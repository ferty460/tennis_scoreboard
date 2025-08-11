package org.example.tennis_scoreboard.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchState {

    private Long matchId;

    private int firstPlayerPoints;
    private int secondPlayerPoints;

    private int firstPlayerGames;
    private int secondPlayerGames;

    private int firstPlayerSets;
    private int secondPlayerSets;

    private boolean isFinished = false;

}
