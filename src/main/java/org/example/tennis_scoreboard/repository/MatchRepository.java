package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.model.Match;

import java.util.List;

public interface MatchRepository extends CrudRepository<Long, Match> {

    List<Match> findAllByWinnerIsNotNull();

}
