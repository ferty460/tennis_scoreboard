package org.example.tennis_scoreboard.service;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.repository.MatchRepository;

import java.util.List;

@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public void save(Match match) {
        matchRepository.save(match);
    }

    public List<Match> getAllFinishedMatches() {
        return matchRepository.findAllByWinnerIsNotNull();
    }

}
