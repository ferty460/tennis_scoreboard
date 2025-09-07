package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.http.HttpServlet;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.exception.ContextException;

import java.lang.reflect.Field;

public abstract class InjectableHttpServlet extends HttpServlet {

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("applicationContext");

        if (context == null) {
            throw new ContextException("ApplicationContext is null!");
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (context.getBeans().containsKey(field.getType())) {
                try {
                    field.setAccessible(true);
                    field.set(this, context.getBean(field.getType()));
                } catch (Exception e) {
                    throw new ContextException("Failed to inject dependency: " + field.getName());
                }
            }
        }
    }

}
