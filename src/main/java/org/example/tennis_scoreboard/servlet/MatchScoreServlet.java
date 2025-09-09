package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.dto.MatchStateResponse;
import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.model.MatchState;
import org.example.tennis_scoreboard.service.MatchScoreCalculationService;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;
import org.example.tennis_scoreboard.service.PlayerService;
import org.example.tennis_scoreboard.util.TennisScoreConverter;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends InjectableHttpServlet {

    private MatchScoreCalculationService calculationService;
    private MatchStorageService matchStorageService;
    private MatchService matchService;
    private PlayerService playerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuidStr = req.getParameter("uuid");
        UUID uuid = UUID.fromString(uuidStr);

        MatchState matchState = matchStorageService.getMatchState(uuid);
        MatchDto match = matchService.getById(matchState.getMatchId());

        MatchStateResponse response = getMatchStateResponse(uuid, matchState, match);

        if (matchState.isFinished()) {
            calculationService.finishMatch(uuid);
            req.setAttribute("winner", match.winner());
        }
        req.setAttribute("isFinished", matchState.isFinished());
        req.setAttribute("state", response);
        req.getRequestDispatcher("WEB-INF/match_score.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuidStr = req.getParameter("uuid");
        String playerIdStr = req.getParameter("playerId");
        UUID matchUuid = UUID.fromString(uuidStr);

        MatchState matchState = matchStorageService.getMatchState(matchUuid);
        PlayerDto winner = playerService.getById(Long.parseLong(playerIdStr));
        MatchDto match = matchService.getById(matchState.getMatchId());

        calculationService.pointWon(matchUuid, match, winner);
        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchUuid);
    }

    private MatchStateResponse getMatchStateResponse(UUID matchUuid, MatchState matchState, MatchDto matchDto) {
        String[] scores = TennisScoreConverter.convertPointsToTennisScore(matchState).split("-");
        String firstPlayerPoints = scores[0];
        String secondPlayerPoints = scores[1];

        return new MatchStateResponse(
                matchUuid, matchDto, matchState,
                firstPlayerPoints, secondPlayerPoints
        );
    }

}
