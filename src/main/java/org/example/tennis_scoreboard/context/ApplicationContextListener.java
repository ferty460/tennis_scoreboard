package org.example.tennis_scoreboard.context;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.config.AppConfig;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.MigrationRunner;
import org.h2.tools.Server;

import java.sql.SQLException;

@Slf4j
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationRunner.migrate();

        ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
        sce.getServletContext().setAttribute("applicationContext", applicationContext);

        // dev only, todo: remove
        try {
            Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        log.info("Application context initialized successfully!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("applicationContext");
        HibernateUtil.shutdown();

        log.info("Application context destroyed successfully!");
    }

}
