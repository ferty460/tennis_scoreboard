package org.example.tennis_scoreboard.dto;

public record MatchStateResponse(
        String firstPlayerName, String secondPlayerName,
        int firstPlayerSets, int secondPlayerSets,
        int firstPlayerGames, int secondPlayerGames,
        int firstPlayerPoints, int secondPlayerPoints
) {

}
