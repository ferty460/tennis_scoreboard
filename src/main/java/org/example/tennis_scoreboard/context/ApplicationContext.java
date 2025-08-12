package org.example.tennis_scoreboard.context;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApplicationContext {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public ApplicationContext(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(ComponentScan.class)) {
            throw new RuntimeException("No @ComponentScan on config class");
        }

        String packageName = configClass.getAnnotation(ComponentScan.class).value();
        try {
            scanPackage(packageName);
            injectDependencies();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getBean(Class<T> type) {
        if (beans.containsKey(type)) {
            return type.cast(beans.get(type));
        }
        throw new RuntimeException("No bean found for type " + type);
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
                    Object instance = createBean(clazz);
                    registerBean(clazz, instance);
                }
            }
            if (file.isDirectory()) {
                scanPackage(packageName + "." + file.getName());
            }
        }
    }

    private void injectDependencies() throws IllegalAccessException {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    field.setAccessible(true);
                    field.set(bean, dependency);
                }
            }
        }
    }

    private Object createBean(Class<?> clazz) throws Exception {
        Constructor<?> targetConstructor = getConstructor(clazz);

        Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            dependencies[i] = getBean(parameterTypes[i]);
            if (dependencies[i] == null) {
                dependencies[i] = createBean(parameterTypes[i]);
                registerBean(parameterTypes[i], dependencies[i]);
            }
        }

        targetConstructor.setAccessible(true);
        return targetConstructor.newInstance(dependencies);
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
