package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MatchStorageService {

    private final Map<UUID, MatchState> matches;

    public MatchStorageService() {
        matches = new HashMap<>();
    }

    public void createMatch(UUID uuid, Match match) {
        matches.put(uuid, MatchState.builder()
                .matchId(match.getId())
                .build());
    }

    public MatchState getMatchState(UUID uuid) {
        if (matches.containsKey(uuid)) {
            return matches.get(uuid);
        }
        throw new IllegalStateException("Match not found");
    }

    public void updateMatchState(UUID uuid, MatchState match) {
        matches.put(uuid, match);
    }

    public void removeMatch(UUID uuid) {
        matches.remove(uuid);
    }

}
