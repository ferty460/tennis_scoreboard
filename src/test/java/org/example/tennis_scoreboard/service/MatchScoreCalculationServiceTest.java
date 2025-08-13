package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;
import org.example.tennis_scoreboard.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MatchScoreCalculationServiceTest {

    private MatchStorageService matchStorageService;
    private MatchScoreCalculationService scoreService;

    private Player p1;
    private Player p2;
    private Match match;
    private UUID uuid;

    @BeforeEach
    void setUp() {
        MatchService matchService = mock(MatchService.class);
        matchStorageService = new MatchStorageService();
        scoreService = new MatchScoreCalculationService(matchService, matchStorageService);

        p1 = new Player(1L, "Fedorino Cappuccino");
        p2 = new Player(2L, "Sudak Tudak");

        uuid = UUID.randomUUID();
        match = Match.builder()
                .id(1L)
                .firstPlayer(p1)
                .secondPlayer(p2)
                .build();

        matchStorageService.createMatch(uuid, match);
    }

    @Nested
    @DisplayName("Match score scenarios")
    class MatchScoreScenarios {

        @Test
        @DisplayName("Given ongoing match — when first player wins a point — then points increase by one")
        void givenOngoingMatch_whenFirstPlayerWinsPoint_thenPointsIncreaseByOne() {
            MatchState initialState = matchStorageService.getMatchState(uuid);
            assertThat(initialState.getFirstPlayerPoints()).isZero();

            scoreService.pointWon(uuid, match, p1);

            int firstPlayerPoints = matchStorageService.getMatchState(uuid).getFirstPlayerPoints();
            assertThat(firstPlayerPoints)
                    .as("The first player must get +1 point")
                    .isEqualTo(1);
        }

        @Test
        @DisplayName("Given ongoing match — when first player wins a game — then games increase by one")
        void givenOngoingMatch_whenFirstPlayerWinsGame_thenGamesIncreaseByOne() {
            MatchState initialState = matchStorageService.getMatchState(uuid);
            assertThat(initialState.getFirstPlayerGames()).isZero();

            addOneGame(p1);

            int firstPlayerGames = matchStorageService.getMatchState(uuid).getFirstPlayerGames();
            assertThat(firstPlayerGames)
                    .as("The first player must get +1 game")
                    .isEqualTo(1);
        }

        @Test
        @DisplayName("Given ongoing match — when first player wins a set — then sets increase by one")
        void givenOngoingMatch_whenFirstPlayerWinsSet_thenSetsIncreaseByOne() {
            MatchState initialState = matchStorageService.getMatchState(uuid);
            assertThat(initialState.getFirstPlayerSets()).isZero();

            addOneSet(p1);

            int firstPlayerSets = matchStorageService.getMatchState(uuid).getFirstPlayerSets();
            assertThat(firstPlayerSets)
                    .as("The first player must get +1 set")
                    .isEqualTo(1);
        }

        @Test
        @DisplayName("Given set score 5–4 — when first player wins game — then set is awarded to first player")
        void givenSetScoreSixFour_whenFirstPlayerWinsGame_thenSetIsAwarded() {
            for (int i = 0; i < 4; i++) {
                addOneGame(p1);
                addOneGame(p2);
            }
            addOneGame(p1);

            addOneGame(p1);

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerSets()).isEqualTo(1);
            assertThat(state.getSecondPlayerSets()).isEqualTo(0);
        }

        @Test
        @DisplayName("Given ongoing match — when second player wins two sets — then second player is winner")
        void givenOngoingMatch_whenSecondPlayerWinsTwoSets_thenWinnerIsSecondPlayer() {
            for (int set = 0; set < 2; set++) {
                for (int i = 0; i < 6; i++) {
                    addOneGame(p2);
                }
            }

            MatchState state = matchStorageService.getMatchState(uuid);

            assertThat(match.getWinner()).isEqualTo(p2);
            assertThat(state.isFinished()).isTrue();
        }

        @Test
        @DisplayName("Given two sets won — when match is finished — then further points are not counted")
        void givenTwoSetsWon_whenFirstPlayerWinsMatch_thenNoFurtherPointsAreCounted() {
            for (int set = 0; set < 2; set++) {
                for (int i = 0; i < 6; i++) {
                    addOneGame(p1);
                }
            }
            assertThat(matchStorageService.getMatchState(uuid).isFinished()).isTrue();

            assertThatThrownBy(() -> scoreService.pointWon(uuid, match, p1))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Match is not active");
        }

        @Test
        @DisplayName("Given match not ongoing — when pointWon is called — then exception is thrown")
        void givenMatchNotOngoing_whenPointWonCalled_thenExceptionThrown() {
            matchStorageService.removeMatch(uuid);

            assertThatThrownBy(() -> scoreService.pointWon(uuid, match, p1))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Match not found");
        }

    }

    @Nested
    @DisplayName("Deuce scenarios")
    class DeuceSituation {

        @Test
        @DisplayName("Given deuce — when players alternate points — then remain tied at deuce")
        void givenDeuceSituation_whenPlayersAlternatePoints_thenRemainTiedAtDeuce() {
            givePreDeuceSituation();

            scoreService.pointWon(uuid, match, p1);
            scoreService.pointWon(uuid, match, p2);

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerPoints()).isEqualTo(4);
            assertThat(state.getSecondPlayerPoints()).isEqualTo(4);
            assertThat(state.getFirstPlayerGames()).isEqualTo(0);
            assertThat(state.getSecondPlayerGames()).isEqualTo(0);
        }

        @Test
        @DisplayName("Given deuce — when first player takes two points in a row — then wins the game")
        void givenDeuceSituation_whenFirstPlayerTakesTwoPointsInARow_thenWinsTheGame() {
            givePreDeuceSituation();

            scoreService.pointWon(uuid, match, p1);
            scoreService.pointWon(uuid, match, p1);

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerPoints()).isEqualTo(0);
            assertThat(state.getSecondPlayerPoints()).isEqualTo(0);
            assertThat(state.getFirstPlayerGames()).isEqualTo(1);
            assertThat(state.getSecondPlayerGames()).isEqualTo(0);
        }

        @Test
        @DisplayName("Given extended deuce — when difference less than two — then no game awarded")
        void givenExtendedDeuce_whenDifferenceLessThanTwo_thenNoGameAwarded() {
            givePreDeuceSituation();

            scoreService.pointWon(uuid, match, p1); // AD p1
            scoreService.pointWon(uuid, match, p2); // Deuce
            scoreService.pointWon(uuid, match, p2); // AD p2
            scoreService.pointWon(uuid, match, p1); // Deuce

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerGames()).isZero();
            assertThat(state.getSecondPlayerGames()).isZero();
        }

    }

    @Nested
    @DisplayName("Tie-break scenarios")
    class TieBreakSituation {

        @Test
        @DisplayName("Given tie-break — when players alternate games — then remain tied at tie-break")
        void givenTieBreakSituation_whenPlayersAlternateGames_thenRemainTiedAtTieBreak() {
            givePreTieBreakSituation();

            addOneGame(p1);
            addOneGame(p2);

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerGames()).isEqualTo(6);
            assertThat(state.getSecondPlayerGames()).isEqualTo(6);
            assertThat(state.getFirstPlayerSets()).isEqualTo(0);
            assertThat(state.getSecondPlayerSets()).isEqualTo(0);
        }

        @Test
        @DisplayName("Given tie-break — when first player wins two games in a row — then wins the set")
        void givenTieBreakSituation_whenFirstPlayerTakesTwoGameInARow_thenWinsTheSet() {
            givePreTieBreakSituation();

            addOneGame(p1);
            addOneGame(p1);

            MatchState state = matchStorageService.getMatchState(uuid);
            assertThat(state.getFirstPlayerGames()).isEqualTo(0);
            assertThat(state.getSecondPlayerGames()).isEqualTo(0);
            assertThat(state.getFirstPlayerSets()).isEqualTo(1);
            assertThat(state.getSecondPlayerSets()).isEqualTo(0);
        }

    }

    private void addOneGame(Player player) {
        for (int i = 0; i < 4; i++) {
            scoreService.pointWon(uuid, match, player);
        }
    }

    private void addOneSet(Player player) {
        for (int i = 0; i < 6; i++) {
            addOneGame(player);
        }
    }

    private void givePreDeuceSituation() {
        for (int i = 0; i < 3; i++) {
            scoreService.pointWon(uuid, match, p1);
            scoreService.pointWon(uuid, match, p2);
        }
    }

    private void givePreTieBreakSituation() {
        for (int i = 0; i < 5; i++) {
            addOneGame(p1);
            addOneGame(p2);
        }
    }

}


