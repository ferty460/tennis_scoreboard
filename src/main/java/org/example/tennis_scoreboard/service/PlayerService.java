package org.example.tennis_scoreboard.service;

import lombok.RequiredArgsConstructor;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.PlayerRepository;

import java.util.List;

@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player getById(long id) {
        if (playerRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Player with id " + id + " not found");
        }

        return playerRepository.findById(id).get();
    }

    public Player save(Player player) {
        return playerRepository.save(player);
    }

    public void update(Player player) {
        playerRepository.update(player);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

}
