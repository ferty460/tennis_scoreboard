package org.example.tennis_scoreboard.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.exception.ContextException;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ApplicationContext {

    @Getter
    private final Map<Class<?>, Object> beans = new HashMap<>();
    private final List<Class<?>> beanClasses = new ArrayList<>();

    public ApplicationContext(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(ComponentScan.class)) {
            throw new ContextException("No @ComponentScan on config class");
        }

        String packageName = configClass.getAnnotation(ComponentScan.class).value();
        try {
            scanPackage(packageName);
            createAllBeans();
            injectDependencies();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ContextException(e.getMessage());
        }
    }

    public <T> T getBean(Class<T> type) {
        if (beans.containsKey(type)) {
            return type.cast(beans.get(type));
        }
        throw new ContextException("No bean found for type " + type);
    }

    private void scanPackage(String packageName) throws Exception {
        String packagePath = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        if (resource == null) {
            throw new RuntimeException("Package not found " + packagePath);
        }

        File directory = new File(resource.getFile());
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Component.class)) {
                    beanClasses.add(clazz);
                }
            }
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            }
        }
    }

    private void createAllBeans() throws Exception {
        for (Class<?> clazz : beanClasses) {
            if (!beans.containsKey(clazz)) {
                createBean(clazz);
            }
        }
    }

    private void createBean(Class<?> clazz) throws Exception {
        if (beans.containsKey(clazz)) {
            return;
        }

        Constructor<?> targetConstructor = getConstructor(clazz);
        Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> dependencyType = parameterTypes[i];

            if (!beans.containsKey(dependencyType)) {
                Class<?> dependencyClass = findBeanClassByType(dependencyType);
                if (dependencyClass != null) {
                    createBean(dependencyClass);
                } else {
                    throw new RuntimeException("No bean class found for dependency type: " + dependencyType);
                }
            }
            dependencies[i] = beans.get(dependencyType);
        }

        targetConstructor.setAccessible(true);
        Object instance = targetConstructor.newInstance(dependencies);
        registerBean(clazz, instance);
    }

    private Class<?> findBeanClassByType(Class<?> type) {
        for (Class<?> clazz : beanClasses) {
            if (type.isAssignableFrom(clazz)) {
                return clazz;
            }
        }
        return null;
    }

    private void injectDependencies() throws IllegalAccessException {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    if (dependency == null) {
                        throw new RuntimeException("No bean found for field injection: " + field.getType());
                    }
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }
            }
        }
    }

    private void registerBean(Class<?> clazz, Object instance) {
        beans.put(clazz, instance);
        for (Class<?> interfacee : clazz.getInterfaces()) {
            beans.put(interfacee, instance);
        }
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> targetConstructor = null;

        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                targetConstructor = constructor;
                break;
            }
        }
        if (targetConstructor == null) {
            if (constructors.length == 1) {
                targetConstructor = constructors[0];
            } else {
                targetConstructor = clazz.getDeclaredConstructor();
            }
        }
        return targetConstructor;
    }

}