package org.example.tennis_scoreboard.service;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;
import org.example.tennis_scoreboard.model.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class MatchScoreCalculationService {

    private final MatchService matchService;
    private final MatchStorageService matchStorageService;

    public void pointWon(UUID matchUuid, Match match, Player winner) {
        MatchState state = matchStorageService.getMatchState(matchUuid);

        if (state == null || state.isFinished()) {
            throw new IllegalStateException("Match is not active");
        }

        boolean isFirst = winner.equals(match.getFirstPlayer());
        if (isFirst) {
            state.setFirstPlayerPoints(state.getFirstPlayerPoints() + 1);
        } else {
            state.setSecondPlayerPoints(state.getSecondPlayerPoints() + 1);
        }

        checkGameWinner(matchUuid, match, state);
        matchStorageService.updateMatchState(matchUuid, state);
    }

    private void checkGameWinner(UUID matchUuid, Match match, MatchState state) {
        int firstPlayerPoints = state.getFirstPlayerPoints();
        int secondPlayerPoints = state.getSecondPlayerPoints();

        if ((firstPlayerPoints >= 4 || secondPlayerPoints >= 4) && Math.abs(firstPlayerPoints - secondPlayerPoints) >= 2) {
            if (firstPlayerPoints > secondPlayerPoints) {
                state.setFirstPlayerGames(state.getFirstPlayerGames() + 1);
            } else {
                state.setSecondPlayerGames(state.getSecondPlayerGames() + 1);
            }

            state.setFirstPlayerPoints(0);
            state.setSecondPlayerPoints(0);

            checkSetWinner(matchUuid, match, state);
        }
    }

    private void checkSetWinner(UUID matchUuid, Match match, MatchState state) {
        int firstPlayerGames = state.getFirstPlayerGames();
        int secondPlayerGames = state.getSecondPlayerGames();

        if ((firstPlayerGames >= 6 || secondPlayerGames >= 6) && Math.abs(firstPlayerGames - secondPlayerGames) >= 2) {
            if (firstPlayerGames > secondPlayerGames) {
                state.setFirstPlayerSets(state.getFirstPlayerSets() + 1);
            } else {
                state.setSecondPlayerSets(state.getSecondPlayerSets() + 1);
            }

            state.setFirstPlayerGames(0);
            state.setSecondPlayerGames(0);

            checkMatchWinner(matchUuid, match, state);
        }
    }

    private void checkMatchWinner(UUID matchUuid, Match match, MatchState state) {
        if (state.getFirstPlayerSets() == 2) {
            match.setWinner(match.getFirstPlayer());
            state.setFinished(true);
            finishMatch(matchUuid, match);
        } else if (state.getSecondPlayerSets() == 2) {
            match.setWinner(match.getSecondPlayer());
            state.setFinished(true);
            finishMatch(matchUuid, match);
        }
    }

    private void finishMatch(UUID matchUuid, Match match) {
        matchService.save(match);
        matchStorageService.removeMatch(matchUuid);
    }

}
