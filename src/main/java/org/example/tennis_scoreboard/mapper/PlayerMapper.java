package org.example.tennis_scoreboard.mapper;

import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDto toDto(Player player);

    Player toEntity(PlayerDto dto);

}
