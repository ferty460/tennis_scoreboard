package org.example.tennis_scoreboard.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.config.AppConfig;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.MigrationRunner;

@Slf4j
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationRunner.migrate();

        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute("applicationContext", applicationContext);

        log.info("Application context initialized successfully!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("applicationContext");
        HibernateUtil.shutdown();

        log.info("Application context destroyed successfully!");
    }

}
