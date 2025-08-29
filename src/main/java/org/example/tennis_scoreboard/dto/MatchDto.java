package org.example.tennis_scoreboard.dto;

public record MatchDto(
        Long id,
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,
        PlayerDto winner
) {
}
