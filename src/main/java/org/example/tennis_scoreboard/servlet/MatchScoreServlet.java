package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.dto.MatchStateResponse;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.MatchState;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.service.MatchScoreCalculationService;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;
import org.example.tennis_scoreboard.service.PlayerService;
import org.example.tennis_scoreboard.util.TennisScoreConverter;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private MatchScoreCalculationService calculationService;
    private MatchStorageService matchStorageService;
    private MatchService matchService;
    private PlayerService playerService;

    @Override
    public void init() throws ServletException {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        if (context == null) {
            throw new ServletException("ApplicationContext is null!");
        }

        this.calculationService = context.getBean(MatchScoreCalculationService.class);
        this.matchStorageService = context.getBean(MatchStorageService.class);
        this.matchService = context.getBean(MatchService.class);
        this.playerService = context.getBean(PlayerService.class);

        // log:
        System.out.println("[MatchScoreServlet] Servlet dependencies injected successfully!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuidStr = req.getParameter("uuid");
        UUID uuid = UUID.fromString(uuidStr);

        MatchState matchState = matchStorageService.getMatchState(uuid);
        Match match = matchService.getMatchById(matchState.getMatchId());

        MatchStateResponse response = getMatchStateResponse(uuid, matchState, match);

        req.setAttribute("matchState", response);
        req.getRequestDispatcher("match_score.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uuidStr = req.getParameter("uuid");
        String playerIdStr = req.getParameter("playerId");
        UUID matchUuid = UUID.fromString(uuidStr);

        MatchState matchState = matchStorageService.getMatchState(matchUuid);
        Player winner = playerService.getById(Long.parseLong(playerIdStr));
        Match match = matchService.getMatchById(matchState.getMatchId());

        calculationService.pointWon(matchUuid, match, winner);
        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchUuid);
    }

    private MatchStateResponse getMatchStateResponse(UUID matchUuid, MatchState matchState, Match match) {
        long firstPlayerId = match.getFirstPlayer().getId();
        long secondPlayerId = match.getSecondPlayer().getId();
        String firstPlayerName = match.getFirstPlayer().getName();
        String secondPlayerName = match.getSecondPlayer().getName();
        int firstPlayerSets = matchState.getFirstPlayerSets();
        int secondPlayerSets = matchState.getSecondPlayerSets();
        int firstPlayerGames = matchState.getFirstPlayerGames();
        int secondPlayerGames = matchState.getSecondPlayerGames();
        int firstPlayerPoints = matchState.getFirstPlayerPoints();
        int secondPlayerPoints = matchState.getSecondPlayerPoints();

        String[] scores = TennisScoreConverter.convertPointsToTennisScore(firstPlayerPoints, secondPlayerPoints).split("-");
        String firstPlayerPointsStr = scores[0];
        String secondPlayerPointsStr = scores[1];

        return new MatchStateResponse(
                matchUuid,
                firstPlayerId, secondPlayerId,
                firstPlayerName, secondPlayerName,
                firstPlayerSets, secondPlayerSets,
                firstPlayerGames, secondPlayerGames,
                firstPlayerPointsStr, secondPlayerPointsStr
        );
    }

}
