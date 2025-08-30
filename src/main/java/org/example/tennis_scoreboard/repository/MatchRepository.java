package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.model.Match;

import java.util.List;

public interface MatchRepository extends CrudRepository<Long, Match> {

    List<Match> findAllByPlayerNamePaged(String playerName, int page);

    List<Match> findAllByPlayerName(String playerName);

    List<Match> findAllPaged(int page);

    long countAll();

    long countAllByPlayerName(String playerName);

}
