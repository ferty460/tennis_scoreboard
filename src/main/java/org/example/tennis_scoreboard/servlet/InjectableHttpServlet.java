package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.ApplicationContext;

import java.lang.reflect.Field;

@Slf4j
public abstract class InjectableHttpServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("applicationContext");

        if (context == null) {
            log.error("ApplicationContext is null! Servlet initialization failed.");
            throw new ServletException("ApplicationContext is null!");
        }

        for (Field field : this.getClass().getDeclaredFields()) {
            if (context.getBeans().containsKey(field.getType())) {
                try {
                    field.setAccessible(true);
                    field.set(this, context.getBean(field.getType()));
                } catch (Exception e) {
                    log.error("Failed to inject dependency! Servlet initialization failed.");
                    throw new ServletException("Failed to inject dependency: " + field.getName(), e);
                }
            }
        }
    }

}
