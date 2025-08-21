package org.example.tennis_scoreboard.dto;

import java.util.UUID;

public record MatchStateResponse(
        UUID matchUuid,
        long firstPlayerId, long secondPlayerId,
        String firstPlayerName, String secondPlayerName,
        int firstPlayerSets, int secondPlayerSets,
        int firstPlayerGames, int secondPlayerGames,
        String firstPlayerPoints, String secondPlayerPoints
) {

}
