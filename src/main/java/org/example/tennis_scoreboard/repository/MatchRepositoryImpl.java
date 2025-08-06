package org.example.tennis_scoreboard.repository;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.QMatch;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {

    private static final QMatch MATCH = QMatch.match;

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Match> findById(Long id) {
        return Optional.ofNullable(new JPAQuery<Match>(sessionFactory.getCurrentSession())
                .select(MATCH)
                .from(MATCH)
                .where(MATCH.id.eq(id))
                .fetchOne()
        );
    }

    @Override
    public List<Match> findAll() {
        return new JPAQuery<Match>(sessionFactory.getCurrentSession())
                .select(MATCH)
                .from(MATCH)
                .fetch();
    }

    @Override
    public Match save(Match entity) {
        return sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void update(Match entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public void delete(Match entity) {
        sessionFactory.getCurrentSession().remove(entity);
    }

}
