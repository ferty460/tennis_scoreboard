package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;

import java.util.HashMap;
import java.util.Map;

public class MatchStorageService {

    private final Map<Long, MatchState> matches;

    public MatchStorageService() {
        matches = new HashMap<>();
    }

    public void createMatch(Match match) {
        matches.put(match.getId(), MatchState.builder()
                .matchId(match.getId())
                .build());
    }

    public MatchState getMatchState(long id) {
        if (matches.containsKey(id)) {
            return matches.get(id);
        }
        throw new IllegalStateException("Match not found");
    }

    public void updateMatchState(MatchState match) {
        matches.put(match.getMatchId(), match);
    }

    public void removeMatch(long id) {
        matches.remove(id);
    }

}
