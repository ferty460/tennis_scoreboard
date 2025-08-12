package org.example.tennis_scoreboard;

import org.example.tennis_scoreboard.config.AppConfig;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.MatchRepository;
import org.example.tennis_scoreboard.repository.MatchRepositoryImpl;
import org.example.tennis_scoreboard.repository.PlayerRepository;
import org.example.tennis_scoreboard.repository.PlayerRepositoryImpl;
import org.example.tennis_scoreboard.service.PlayerService;
import org.example.tennis_scoreboard.util.DataImporter;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(AppConfig.class);
        PlayerService playerService = context.getBean(PlayerService.class);
        Player player = new Player();
        player.setName("John Doe");
        playerService.save(player);

        /*try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                PlayerRepository playerRepository = new PlayerRepositoryImpl(session);
                MatchRepository matchRepository = new MatchRepositoryImpl(session);
                DataImporter importer = new DataImporter(matchRepository, playerRepository);

                importer.importData();

                session.getTransaction().commit();
            }
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                MatchRepository matchRepository = new MatchRepositoryImpl(session);
                System.out.println(matchRepository.findAll());

                session.getTransaction().commit();
            }
        }*/
    }

}
