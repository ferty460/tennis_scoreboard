package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.repository.MatchRepository;

import java.util.List;
import java.util.Optional;

@Component
public class MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public void save(Match match) {
        matchRepository.save(match);
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public List<Match> getAllFinishedMatches() {
        return matchRepository.findAllByWinnerIsNotNull();
    }

    public Optional<Match> getMatchById(long id) {
        return matchRepository.findById(id);
    }

    public void update(Match match) {
        matchRepository.update(match);
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

}
