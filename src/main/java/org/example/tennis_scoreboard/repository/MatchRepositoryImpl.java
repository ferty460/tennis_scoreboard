package org.example.tennis_scoreboard.repository;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Match;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {

    private final Session session;

    @Override
    public Optional<Match> findById(Long id) {
        return Optional.ofNullable(
                session.find(Match.class, id)
        );
    }

    @Override
    public List<Match> findAll() {
        return session.createQuery(
                "select m from Match m", Match.class
        ).getResultList();
    }

    @Override
    public Match save(Match entity) {
        session.persist(entity);
        return entity;
    }

    @Override
    public void update(Match entity) {
        session.merge(entity);
    }

    @Override
    public void delete(Match entity) {
        session.remove(entity);
    }

    @Override
    public List<Match> findAllByWinnerIsNotNull() {
        return session.createQuery(
                "select m from Match m where winner is not null", Match.class
        ).getResultList();
    }
}
