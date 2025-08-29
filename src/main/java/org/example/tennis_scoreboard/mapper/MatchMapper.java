package org.example.tennis_scoreboard.mapper;

import org.example.tennis_scoreboard.dto.FinishedMatchDto;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.model.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MatchMapper {

    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDto toDto(Match match);

    Match toEntity(MatchDto matchDto);

    @Mapping(source = "firstPlayer.name", target = "firstPlayerName")
    @Mapping(source = "secondPlayer.name", target = "secondPlayerName")
    @Mapping(source = "winner.name", target = "winnerName")
    FinishedMatchDto toFinishedMatchDto(Match match);

    List<FinishedMatchDto> toFinishedMatchDtoList(List<Match> matches);

}
