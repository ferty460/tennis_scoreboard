package org.example.tennis_scoreboard.util;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    private static final String HIBERNATE_CONFIG_FILE = "hibernate.cfg.xml";

    @Getter
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure(HIBERNATE_CONFIG_FILE)
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Match.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

}
