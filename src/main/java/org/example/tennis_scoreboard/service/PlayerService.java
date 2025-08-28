package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player getById(long id) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            return playerOptional.get();
        }

        throw new RuntimeException("Player with id " + id + " not found");
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
