package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TennisScoreConverter {

    public static String convertPointsToTennisScore(int firstPlayerPoints, int secondPlayerPoints) {
        if (firstPlayerPoints >= 3 && secondPlayerPoints >= 3) {
            if (firstPlayerPoints == secondPlayerPoints) {
                return "40-40";
            } else if (firstPlayerPoints > secondPlayerPoints) {
                return "AD-40";
            } else {
                return "40-AD";
            }
        } else {
            return convertSinglePoint(firstPlayerPoints) + "-" + convertSinglePoint(secondPlayerPoints);
        }
    }

    private static String convertSinglePoint(int points) {
        return switch (points) {
            case 0 -> "0";
            case 1 -> "15";
            case 2 -> "30";
            case 3 -> "40";
            default -> String.valueOf(points);
        };
    }

}
