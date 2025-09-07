package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.mapper.MatchMapper;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MatchStorageService {

    private final Map<UUID, MatchState> matches;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public MatchStorageService() {
        matches = new HashMap<>();
    }

    public void createMatch(UUID uuid, MatchDto matchDto) {
        Match match = matchMapper.toEntity(matchDto);
        matches.put(uuid, MatchState.builder()
                .matchId(match.getId())
                .build());
    }

    public MatchState getMatchState(UUID uuid) {
        if (matches.containsKey(uuid)) {
            return matches.get(uuid);
        }

        throw new NotFoundException("No match with uuid " + uuid + " or it is already finished");
    }

    public void updateMatchState(UUID uuid, MatchState match) {
        matches.put(uuid, match);
    }

    public void removeMatch(UUID uuid) {
        matches.remove(uuid);
    }

}
