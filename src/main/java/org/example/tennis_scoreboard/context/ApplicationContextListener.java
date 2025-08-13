package org.example.tennis_scoreboard.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.tennis_scoreboard.config.AppConfig;
import org.example.tennis_scoreboard.util.MigrationRunner;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationRunner.migrate();

        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute("applicationContext", applicationContext);

        // log: ApplicationContext initialized
        System.out.println("ApplicationContext initialized!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("applicationContext");

        // log: ApplicationContext destroyed
        System.out.println("ApplicationContext destroyed!");
    }

}
