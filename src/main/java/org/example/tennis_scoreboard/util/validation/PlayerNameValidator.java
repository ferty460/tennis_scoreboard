package org.example.tennis_scoreboard.util.validation;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.PlayersInMatchRequest;
import org.example.tennis_scoreboard.exception.PlayerNameException;

@Component("playerNameValidator")
public class PlayerNameValidator implements Validator<PlayersInMatchRequest, PlayerNameException> {

    private static final int MAX_LENGTH_PLAYER_NAME = 32;
    private static final String PLAYER_NAME_REGEX = "[a-zA-Z0-9а-яА-ЯёЁ\\s]+";

    @Override
    public void validate(PlayersInMatchRequest names) throws PlayerNameException {
        String firstPlayerName = names.firstPlayerName();
        String secondPlayerName = names.secondPlayerName();

        if (firstPlayerName == null || firstPlayerName.isEmpty() ||
                secondPlayerName == null || secondPlayerName.isEmpty()) {
            throw new PlayerNameException("Player names cannot be empty");
        }
        if (firstPlayerName.equals(secondPlayerName)) {
            throw new PlayerNameException("Player names cannot be the same");
        }
        if (firstPlayerName.length() >= MAX_LENGTH_PLAYER_NAME || secondPlayerName.length() >= MAX_LENGTH_PLAYER_NAME) {
            throw new PlayerNameException("Player names cannot be longer than 32 characters");
        }
        if (!firstPlayerName.matches(PLAYER_NAME_REGEX) || !secondPlayerName.matches(PLAYER_NAME_REGEX)) {
            throw new PlayerNameException("The player's name can contain only letters (ru/en), numbers and spaces.");
        }
    }

}
