package org.example.tennis_scoreboard;

import org.example.tennis_scoreboard.repository.MatchRepository;
import org.example.tennis_scoreboard.repository.MatchRepositoryImpl;
import org.example.tennis_scoreboard.repository.PlayerRepository;
import org.example.tennis_scoreboard.repository.PlayerRepositoryImpl;
import org.example.tennis_scoreboard.util.DataImporter;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
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
        }
    }

}
