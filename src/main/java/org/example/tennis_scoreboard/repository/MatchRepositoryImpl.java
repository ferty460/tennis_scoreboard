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
        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery("select m from Match m", Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .getResultList();
        });
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

    @Override
    public List<Match> findAllByWinnerIsNotNull() {
        return TransactionManager.executeReadOnly(sessionFactory, session -> {
            EntityGraph<?> entityGraph = session.getEntityGraph("Match.withPlayers");
            return session.createQuery("select m from Match m where m.winner is not null", Match.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .getResultList();
        });
    }

}
