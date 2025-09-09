package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;
import org.example.tennis_scoreboard.model.MatchState;

@UtilityClass
public class TennisScoreConverter {

    private static final int TIE_BREAK_POINTS = 6;
    private static final int PRE_DEUCE_POINTS = 3;

    public static String convertPointsToTennisScore(MatchState matchState) {
        int firstPlayerPoints = matchState.getFirstPlayerPoints();
        int secondPlayerPoints = matchState.getSecondPlayerPoints();
        int firstPlayerGames = matchState.getFirstPlayerGames();
        int secondPlayerGames = matchState.getSecondPlayerGames();

        if (isTieBreak(firstPlayerGames, secondPlayerGames)) {
            return convertTieBreakPoints(firstPlayerPoints, secondPlayerPoints);
        } else {
            return convertDeuceGamePoints(firstPlayerPoints, secondPlayerPoints);
        }
    }

    private static boolean isTieBreak(int firstPlayerGames, int secondPlayerGames) {
        return firstPlayerGames == TIE_BREAK_POINTS && secondPlayerGames == TIE_BREAK_POINTS;
    }

    private static String convertDeuceGamePoints(int firstPlayerPoints, int secondPlayerPoints) {
        if (firstPlayerPoints >= PRE_DEUCE_POINTS && secondPlayerPoints >= PRE_DEUCE_POINTS) {
            if (firstPlayerPoints == secondPlayerPoints) {
                return "40-40";
            } else if (firstPlayerPoints > secondPlayerPoints) {
                return "AD-40";
            } else {
                return "40-AD";
            }
        } else {
            return convertPoint(firstPlayerPoints) + "-" + convertPoint(secondPlayerPoints);
        }
    }

    private static String convertTieBreakPoints(int firstPlayerPoints, int secondPlayerPoints) {
        return firstPlayerPoints + "-" + secondPlayerPoints;
    }

    private static String convertPoint(int points) {
        return switch (points) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            default -> String.valueOf(points);
        };
    }

}
