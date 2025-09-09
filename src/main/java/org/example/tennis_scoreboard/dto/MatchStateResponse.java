package org.example.tennis_scoreboard.dto;

import org.example.tennis_scoreboard.model.MatchState;

import java.util.UUID;

public record MatchStateResponse(
        UUID matchUuid, MatchDto matchDto, MatchState matchState,
        String firstPlayerPoints, String secondPlayerPoints
) {

}
