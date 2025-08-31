package org.example.tennis_scoreboard.service;

import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.mapper.PlayerMapper;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper mapper = PlayerMapper.INSTANCE;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public PlayerDto getById(long id) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            return mapper.toDto(playerOptional.get());
        }

        log.error("Player with id {} not found", id);
        throw new NotFoundException("Player with id " + id + " not found");
    }

    public PlayerDto save(PlayerDto playerDto) {
        Player player = mapper.toEntity(playerDto);
        player = playerRepository.save(player);
        return mapper.toDto(player);
    }

    public void update(Player player) {
        playerRepository.update(player);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

}
