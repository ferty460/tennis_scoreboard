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

    private void checkGameWinner(MatchDto matchDto, MatchState state) {
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

            checkSetWinner(matchDto, state);
        }
    }

    private void checkSetWinner(MatchDto matchDto, MatchState state) {
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

            checkMatchWinner(matchDto, state);
        }
    }

    private void checkMatchWinner(MatchDto matchDto, MatchState state) {
        if (state.getFirstPlayerSets() == 2) {
            MatchDto updatedMatch = new MatchDto(
                    matchDto.id(),
                    matchDto.firstPlayer(),
                    matchDto.secondPlayer(),
                    matchDto.firstPlayer()
            );
            matchService.update(updatedMatch);
            state.setFinished(true);
        } else if (state.getSecondPlayerSets() == 2) {
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

    public void finishMatch(UUID matchUuid) {
        matchStorageService.removeMatch(matchUuid);
    }

}
