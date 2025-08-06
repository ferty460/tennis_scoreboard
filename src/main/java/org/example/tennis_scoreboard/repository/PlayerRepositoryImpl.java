package org.example.tennis_scoreboard.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.model.QPlayer;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepository {

    private static final QPlayer PLAYER = QPlayer.player;

    private final Session session;

    @Override
    public Optional<Player> findById(Long id) {
        return Optional.ofNullable(new JPAQuery<Player>(session)
                .select(PLAYER)
                .from(PLAYER)
                .where(PLAYER.id.eq(id))
                .fetchOne()
        );
    }

    @Override
    public List<Player> findAll() {
        return new JPAQuery<Player>(session)
                .select(PLAYER)
                .from(PLAYER)
                .fetch();
    }

    @Override
    public Player save(Player entity) {
        return session.merge(entity);
    }

    @Override
    public void update(Player entity) {
        session.persist(entity);
    }

    @Override
    public void delete(Player entity) {
        session.remove(entity);
    }

}
