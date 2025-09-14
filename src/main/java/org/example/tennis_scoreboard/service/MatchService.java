package org.example.tennis_scoreboard.service;

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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.example.tennis_scoreboard.repository.MatchRepositoryImpl.DEFAULT_LIMIT;

@Component
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper mapper = MatchMapper.INSTANCE;

    @Autowired
    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public PaginationResult<FinishedMatchDto> getAllFinishedMatches(String pageStr, String playerName) {
        int page = parsePageNumber(pageStr);
        long totalCount = getTotalCount(playerName);
        validatePageNumber(page, totalCount);

        List<Match> finishedMatches = getFinishedMatches(playerName, page, totalCount);
        List<FinishedMatchDto> matchDtoList = mapper.toFinishedMatchDtoList(finishedMatches);

        return new PaginationResult<>(matchDtoList, page, totalCount, DEFAULT_LIMIT);
    }

    public MatchDto getById(long id) {
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            return mapper.toDto(matchOptional.get());
        }

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

    private int parsePageNumber(String pageStr) {
        if (!StringUtils.hasText(pageStr)) {
            return 1;
        }

        try {
            return Integer.parseInt(pageStr);
        } catch (NumberFormatException e) {
            throw new PaginationException("Page must be a valid number");
        }
    }

    private long getTotalCount(String playerName) {
        return StringUtils.hasText(playerName)
                ? matchRepository.countAllByPlayerName(playerName)
                : matchRepository.countAll();
    }

    private void validatePageNumber(int page, long totalCount) {
        if (totalCount == 0) {
            return;
        }

        int totalPages = calculateTotalPages(totalCount);
        if (page < 1) {
            throw new PaginationException("Page must be greater than 0");
        }
        if (page > totalPages) {
            throw new PaginationException("Page is greater than total pages");
        }
    }

    private int calculateTotalPages(long totalCount) {
        return (int) Math.ceil((double) totalCount / DEFAULT_LIMIT);
    }

    private List<Match> getFinishedMatches(String playerName, int page, long totalCount) {
        if (totalCount == 0) {
            return Collections.emptyList();
        }

        return StringUtils.hasText(playerName)
                ? matchRepository.findAllByPlayerNamePaged(playerName, page)
                : matchRepository.findAllPaged(page);
    }

}
