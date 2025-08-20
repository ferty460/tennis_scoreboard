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
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private MatchStorageService matchStorageService;
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        if (context == null) {
            throw new ServletException("ApplicationContext is null!");
        }

        this.matchStorageService = context.getBean(MatchStorageService.class);
        this.matchService = context.getBean(MatchService.class);

        // log:
        System.out.println("[MatchScoreServlet] Servlet dependencies injected successfully!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuidStr = req.getParameter("uuid");
        UUID uuid = UUID.fromString(uuidStr);

        MatchState matchState = matchStorageService.getMatchState(uuid);
        Match match = matchService.getMatchById(matchState.getMatchId());

        MatchStateResponse response = getMatchStateResponse(matchState, match);

        req.setAttribute("matchState", response);
        req.getRequestDispatcher("match_score.jsp").forward(req, resp);
    }

    private MatchStateResponse getMatchStateResponse(MatchState matchState, Match match) {
        String firstPlayerName = match.getFirstPlayer().getName();
        String secondPlayerName = match.getSecondPlayer().getName();
        int firstPlayerSets = matchState.getFirstPlayerSets();
        int secondPlayerSets = matchState.getSecondPlayerSets();
        int firstPlayerGames = matchState.getFirstPlayerGames();
        int secondPlayerGames = matchState.getSecondPlayerGames();
        int firstPlayerPoints = matchState.getFirstPlayerPoints();
        int secondPlayerPoints = matchState.getSecondPlayerPoints();

        return new MatchStateResponse(
                firstPlayerName, secondPlayerName,
                firstPlayerSets, secondPlayerSets,
                firstPlayerGames, secondPlayerGames,
                firstPlayerPoints, secondPlayerPoints
        );
    }

}
