package org.example.tennis_scoreboard.util;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.MatchRepository;
import org.example.tennis_scoreboard.repository.PlayerRepository;

@RequiredArgsConstructor
public class DataImporter {

    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;

    public void importData() {
        Player steve = savePlayer("Steve");
        Player bob = savePlayer("Bob");
        Player alice = savePlayer("Alice");
        Player charlie = savePlayer("Charlie");
        Player david = savePlayer("David");
        Player eve = savePlayer("Eve");

        saveMatch(steve, bob, steve);
        saveMatch(steve, bob, bob);
        saveMatch(steve, alice, steve);
        saveMatch(david, charlie, david);
        saveMatch(alice, eve, alice);
        saveMatch(david, eve, eve);
    }

    private void saveMatch(Player player1, Player player2, Player winner) {
        Match match = Match.builder()
                .firstPlayer(player1)
                .secondPlayer(player2)
                .winner(winner)
                .build();
        matchRepository.save(match);
    }

    private Player savePlayer(String name) {
        Player player = Player.builder()
                .name(name)
                .build();
        playerRepository.save(player);

        return player;
    }

}
