package org.example.tennis_scoreboard.service;

import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.FinishedMatchDto;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.exception.PaginationException;
import org.example.tennis_scoreboard.mapper.MatchMapper;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.PaginationResult;
import org.example.tennis_scoreboard.repository.MatchRepository;
import org.flywaydb.core.internal.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.example.tennis_scoreboard.repository.MatchRepositoryImpl.DEFAULT_LIMIT;

@Slf4j
@Component
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper mapper = MatchMapper.INSTANCE;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    public PaginationResult<FinishedMatchDto> getAllFinishedMatches(String pageStr, String playerName) {
        int page = 1;
        if (StringUtils.hasText(pageStr)) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                log.error("Invalid page number", e);
                throw new PaginationException(e.getMessage());
            }
        }

        List<Match> finishedMatches;
        long totalCount;

        if (StringUtils.hasText(playerName)) {
            finishedMatches = matchRepository.findAllByPlayerNamePaged(playerName, page);
            totalCount = matchRepository.countAllByPlayerName(playerName);
        } else {
            finishedMatches = matchRepository.findAllPaged(page);
            totalCount = matchRepository.countAll();
        }

        List<FinishedMatchDto> matchDtoList = mapper.toFinishedMatchDtoList(finishedMatches);
        return new PaginationResult<>(matchDtoList, page, totalCount, DEFAULT_LIMIT);
    }

    public MatchDto getById(long id) {
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            return mapper.toDto(matchOptional.get());
        }

        log.error("Match with id {} not found", id);
        throw new NotFoundException("Match with id " + id + " not found");
    }

    public MatchDto save(MatchDto matchDto) {
        Match match = mapper.toEntity(matchDto);
        return mapper.toDto(matchRepository.save(match));
    }

    public void update(MatchDto matchDto) {
        Match match = mapper.toEntity(matchDto);
        matchRepository.update(match);
    }

    public void delete(Match match) {
        matchRepository.delete(match);
    }

}
