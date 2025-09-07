package org.example.tennis_scoreboard.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.example.tennis_scoreboard.util.TransactionManager;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PlayerRepositoryImpl implements PlayerRepository {

    private final SessionFactory sessionFactory;

    public PlayerRepositoryImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Optional<Player> findById(Long id) {
        log.debug("Finding player by id: {}", id);
        return Optional.ofNullable(
                TransactionManager.executeReadOnly(sessionFactory, session ->
                        session.find(Player.class, id)
                )
        );
    }

    @Override
    public Optional<Player> findByName(String name) {
        log.debug("Finding player by name: {}", name);
        return TransactionManager.executeReadOnly(sessionFactory, session ->
                session.createQuery("select p from Player p where p.name = :name", Player.class)
                        .setParameter("name", name)
                        .getResultList()
                        .stream()
                        .findFirst()
        );
    }

    @Override
    public List<Player> findAll() {
        log.debug("Finding all players");
        return TransactionManager.executeReadOnly(sessionFactory, session ->
                session.createQuery("select m from Player m", Player.class)
                        .getResultList()
        );
    }

    @Override
    public Player save(Player entity) {
        log.debug("Saving player: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.persist(entity));
        return entity;
    }

    @Override
    public void update(Player entity) {
        log.debug("Updating player: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.merge(entity));
    }

    @Override
    public void delete(Player entity) {
        log.debug("Deleting player: {}", entity);
        TransactionManager.executeInTransaction(sessionFactory, session -> session.remove(entity));
    }

}
