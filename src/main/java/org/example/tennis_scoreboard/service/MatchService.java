package org.example.tennis_scoreboard.service;

import org.example.tennis_scoreboard.context.Autowired;
import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.dto.FinishedMatchDto;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.mapper.MatchMapper;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.repository.MatchRepository;

import java.util.List;
import java.util.Optional;

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

    public List<FinishedMatchDto> getAllFinishedMatches() {
        List<Match> finishedMatches = matchRepository.findAllByWinnerIsNotNull();
        return mapper.toFinishedMatchDtoList(finishedMatches);
    }

    public MatchDto getById(long id) {
        Optional<Match> matchOptional = matchRepository.findById(id);

        if (matchOptional.isPresent()) {
            return mapper.toDto(matchOptional.get());
        }

        throw new RuntimeException("Match with id " + id + " not found");
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
