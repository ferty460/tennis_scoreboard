package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.mapper.PlayerMapper;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.repository.PlayerRepository;

import java.util.Optional;

@Component
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper mapper = PlayerMapper.INSTANCE;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public PlayerDto getById(long id) {
        Optional<Player> playerOptional = playerRepository.findById(id);

        if (playerOptional.isPresent()) {
            return mapper.toDto(playerOptional.get());
        }

        throw new NotFoundException("Player with id " + id + " not found");
    }

    public PlayerDto save(PlayerDto playerDto) {
        String name = playerDto.name();

        Player player = playerRepository.findByName(name)
                .orElseGet(() -> {
                    Player newPlayer = mapper.toEntity(playerDto);
                    return playerRepository.save(newPlayer);
                });

        return mapper.toDto(player);
    }

}
