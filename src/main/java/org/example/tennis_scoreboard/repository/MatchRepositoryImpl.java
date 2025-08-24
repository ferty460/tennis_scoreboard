package org.example.tennis_scoreboard.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.TransactionManager;
import org.hibernate.SessionFactory;

import java.util.List;
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
        return Optional.ofNullable(
                TransactionManager.executeReadOnly(sessionFactory, session ->
                        session.find(Match.class, id)
                )
        );
    }

    @Override
    public List<Match> findAll() {
        log.debug("Finding all matches");
        return TransactionManager.executeReadOnly(sessionFactory, session ->
                session.createQuery(
                        "select m from Match m " +
                                "left join fetch m.firstPlayer " +
                                "left join fetch m.secondPlayer " +
                                "left join fetch m.winner", Match.class
                ).getResultList()
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

    @Override
    public List<Match> findAllByWinnerIsNotNull() {
        return TransactionManager.executeReadOnly(sessionFactory, session ->
                session.createQuery(
                        "select m from Match m " +
                                "left join fetch m.firstPlayer " +
                                "left join fetch m.secondPlayer " +
                                "left join fetch m.winner " +
                                "where m.winner is not null", Match.class
                ).getResultList()
        );
    }

}
