package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepositoryImpl implements PlayerRepository {

    private final Session session;

    public PlayerRepositoryImpl() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return Optional.ofNullable(
                session.find(Player.class, id)
        );
    }

    @Override
    public List<Player> findAll() {
        return session.createQuery("select p from Player p", Player.class).list();
    }

    @Override
    public Player save(Player entity) {
        session.persist(entity);
        return entity;
    }

    @Override
    public void update(Player entity) {
        session.merge(entity);
    }

    @Override
    public void delete(Player entity) {
        session.remove(entity);
    }

}
