package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.TransactionManager;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepositoryImpl implements PlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return Optional.ofNullable(
                TransactionManager.executeReadOnly(sessionFactory, session ->
                        session.find(Player.class, id)
                )
        );
    }

    @Override
    public List<Player> findAll() {
        return TransactionManager.executeReadOnly(sessionFactory, session ->
                session.createQuery("select m from Player m", Player.class)
                        .getResultList()
        );
    }

    @Override
    public Player save(Player entity) {
        TransactionManager.executeInTransaction(sessionFactory, session -> session.persist(entity));
        return entity;
    }

    @Override
    public void update(Player entity) {
        TransactionManager.executeInTransaction(sessionFactory, session -> session.merge(entity));
    }

    @Override
    public void delete(Player entity) {
        TransactionManager.executeInTransaction(sessionFactory, session -> session.remove(entity));
    }

}
