package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.TransactionManager;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Component
public class MatchRepositoryImpl implements MatchRepository {

    private final SessionFactory sessionFactory;

    public MatchRepositoryImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Optional<Match> findById(Long id) {
        return Optional.ofNullable(
                TransactionManager.executeReadOnly(sessionFactory, session ->
                        session.find(Match.class, id)
                )
        );
    }

    @Override
    public List<Match> findAll() {
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
        TransactionManager.executeInTransaction(sessionFactory, session -> session.persist(entity));
        return entity;
    }

    @Override
    public void update(Match entity) {
        TransactionManager.executeInTransaction(sessionFactory, session -> session.merge(entity));
    }

    @Override
    public void delete(Match entity) {
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
