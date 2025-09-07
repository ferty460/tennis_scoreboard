package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.model.MatchState;

import java.util.Objects;
import java.util.UUID;

@Component
public class MatchScoreCalculationService {

    private static final int MIN_POINTS_TO_WIN_GAME = 4;
    private static final int MIN_POINT_DIFFERENCE_TO_WIN_GAME = 2;
    private static final int MIN_GAMES_TO_WIN_SET = 6;
    private static final int MIN_GAME_DIFFERENCE_TO_WIN_SET = 2;
    private static final int SETS_TO_WIN_MATCH = 2;

    private final MatchService matchService;
    private final MatchStorageService matchStorageService;

    @Autowired
    public MatchScoreCalculationService(MatchService matchService, MatchStorageService matchStorageService) {
        this.matchService = matchService;
        this.matchStorageService = matchStorageService;
    }

    public void pointWon(UUID matchUuid, MatchDto matchDto, PlayerDto winner) {
        MatchState state = matchStorageService.getMatchState(matchUuid);

        if (state == null || state.isFinished()) {
            throw new NotFoundException("No such match or it is already finished");
        }

        boolean isFirst = Objects.equals(winner.id(), matchDto.firstPlayer().id());
        if (isFirst) {
            state.setFirstPlayerPoints(state.getFirstPlayerPoints() + 1);
        } else {
            state.setSecondPlayerPoints(state.getSecondPlayerPoints() + 1);
        }

        checkGameWinner(matchDto, state);
        matchStorageService.updateMatchState(matchUuid, state);
    }

    public void finishMatch(UUID matchUuid) {
        matchStorageService.removeMatch(matchUuid);
    }

    private void checkGameWinner(MatchDto matchDto, MatchState state) {
        int firstPlayerPoints = state.getFirstPlayerPoints();
        int secondPlayerPoints = state.getSecondPlayerPoints();

        if (isFinishedGame(firstPlayerPoints, secondPlayerPoints)) {
            if (firstPlayerPoints > secondPlayerPoints) {
                state.setFirstPlayerGames(state.getFirstPlayerGames() + 1);
            } else {
                state.setSecondPlayerGames(state.getSecondPlayerGames() + 1);
            }

            state.setFirstPlayerPoints(0);
            state.setSecondPlayerPoints(0);

            checkSetWinner(matchDto, state);
        }
    }

    private void checkSetWinner(MatchDto matchDto, MatchState state) {
        int firstPlayerGames = state.getFirstPlayerGames();
        int secondPlayerGames = state.getSecondPlayerGames();

        if (isFinishedSet(firstPlayerGames, secondPlayerGames)) {
            if (firstPlayerGames > secondPlayerGames) {
                state.setFirstPlayerSets(state.getFirstPlayerSets() + 1);
            } else {
                state.setSecondPlayerSets(state.getSecondPlayerSets() + 1);
            }

            state.setFirstPlayerGames(0);
            state.setSecondPlayerGames(0);

            checkMatchWinner(matchDto, state);
        }
    }

    private void checkMatchWinner(MatchDto matchDto, MatchState state) {
        if (state.getFirstPlayerSets() == SETS_TO_WIN_MATCH) {
            MatchDto updatedMatch = new MatchDto(
                    matchDto.id(),
                    matchDto.firstPlayer(),
                    matchDto.secondPlayer(),
                    matchDto.firstPlayer()
            );
            matchService.update(updatedMatch);
            state.setFinished(true);
        } else if (state.getSecondPlayerSets() == SETS_TO_WIN_MATCH) {
            MatchDto updatedMatch = new MatchDto(
                    matchDto.id(),
                    matchDto.firstPlayer(),
                    matchDto.secondPlayer(),
                    matchDto.secondPlayer()
            );
            matchService.update(updatedMatch);
            state.setFinished(true);
        }
    }

    private boolean isFinishedGame(int firstPlayerPoints, int secondPlayerPoints) {
        return (firstPlayerPoints >= MIN_POINTS_TO_WIN_GAME || secondPlayerPoints >= MIN_POINTS_TO_WIN_GAME) &&
                Math.abs(firstPlayerPoints - secondPlayerPoints) >= MIN_POINT_DIFFERENCE_TO_WIN_GAME;
    }

    private boolean isFinishedSet(int firstPlayerGames, int secondPlayerGames) {
        return (firstPlayerGames >= MIN_GAMES_TO_WIN_SET || secondPlayerGames >= MIN_GAMES_TO_WIN_SET) &&
                Math.abs(firstPlayerGames - secondPlayerGames) >= MIN_GAME_DIFFERENCE_TO_WIN_SET;
    }

}
