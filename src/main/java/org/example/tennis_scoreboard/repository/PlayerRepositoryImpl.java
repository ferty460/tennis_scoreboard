package org.example.tennis_scoreboard.repository;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Player;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepository {

    private final Session session;

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
