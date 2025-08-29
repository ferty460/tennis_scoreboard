package org.example.tennis_scoreboard.dto;

public record FinishedMatchDto(
        Long id,
        String firstPlayerName,
        String secondPlayerName,
        String winnerName
) {}
