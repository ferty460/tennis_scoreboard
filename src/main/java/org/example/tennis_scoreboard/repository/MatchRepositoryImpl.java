package org.example.tennis_scoreboard.repository;

import jakarta.persistence.EntityGraph;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.TransactionManager;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class MatchRepositoryImpl implements MatchRepository {

    public static final int DEFAULT_LIMIT = 5;

    private final SessionFactory sessionFactory;

    public MatchRepositoryImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Optional<Match> findById(Long id) {
        log.debug("Finding match by id: {}", id);
        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> graph = session.getEntityGraph("Match.withPlayers");
            return Optional.ofNullable(
                    session.find(Match.class, id, Map.of("jakarta.persistence.fetchgraph", graph))
            );
        });
    }

    @Override
    public List<Match> findAll() {
        log.debug("Finding all matches");

        String hql = """
            select m from Match m
            where m.winner is not null
        """;

        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery(hql, Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .setMaxResults(DEFAULT_LIMIT)
                    .getResultList();
        });
    }

    @Override
    public List<Match> findAllByPlayerNamePaged(String playerName, int page) {
        log.debug("Finding all matches by player name {} on {} page", playerName, page);

        String hql = """
             select m from Match m
             where m.winner is not null and
                (m.firstPlayer.name like :name or m.secondPlayer.name like :name)
        """;
        int offset = (page - 1) * DEFAULT_LIMIT;

        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery(hql, Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .setFirstResult(offset)
                    .setMaxResults(DEFAULT_LIMIT)
                    .setParameter("name", "%" + playerName + "%")
                    .getResultList();
        });
    }

    @Override
    public List<Match> findAllByPlayerName(String playerName) {
        log.debug("Finding all matches by player name: {}", playerName);

        String hql = """
             select m from Match m
             where m.winner is not null and
                (m.firstPlayer.name like :name or m.secondPlayer.name like :name)
        """;

        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery(hql, Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .setParameter("name", "%" + playerName + "%")
                    .setMaxResults(DEFAULT_LIMIT)
                    .getResultList();
        });
    }

    @Override
    public List<Match> findAllPaged(int page) {
        log.debug("Finding all matches on page {}", page);

        String hql = """
             select m from Match m
             where m.winner is not null
        """;
        int offset = (page - 1) * DEFAULT_LIMIT;

        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery(hql, Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .setFirstResult(offset)
                    .setMaxResults(DEFAULT_LIMIT)
                    .getResultList();
        });
    }

    @Override
    public long countAll() {
        log.debug("Counting all matches");

        String hql = """
            select count(m) from Match m
            where m.winner is not null
        """;

        return TransactionManager.executeReadOnly(
                sessionFactory,
                session -> session.createQuery(hql, Long.class)
                        .getSingleResult()
        );
    }

    @Override
    public long countAllByPlayerName(String playerName) {
        log.debug("Counting matches by player name: {}", playerName);

        String hql = """
            select count(m) from Match m
            where m.winner is not null and
               (m.firstPlayer.name like :name or m.secondPlayer.name like :name)
        """;

        return TransactionManager.executeReadOnly(
                sessionFactory,
                session -> session.createQuery(hql, Long.class)
                        .setParameter("name", "%" + playerName + "%")
                        .getSingleResult()
        );
    }

    @Override
    public Match save(Match entity) {
        log.debug("Saving match: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.persist(entity));
        return entity;
    }

    @Override
    public void update(Match entity) {
        log.debug("Updating match: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.merge(entity));
    }

    @Override
    public void delete(Match entity) {
        log.debug("Deleting match: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.remove(entity));
    }

}
