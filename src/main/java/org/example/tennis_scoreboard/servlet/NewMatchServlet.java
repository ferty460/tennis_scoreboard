package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.dto.PlayersInMatchRequest;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;
import org.example.tennis_scoreboard.service.PlayerService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    private MatchStorageService matchStorageService;
    private MatchService matchService;
    private PlayerService playerService;

    @Override
    public void init() throws ServletException {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        if (context == null) {
            throw new ServletException("ApplicationContext is null!");
        }

        this.matchStorageService = context.getBean(MatchStorageService.class);
        this.matchService = context.getBean(MatchService.class);
        this.playerService = context.getBean(PlayerService.class);

        // log:
        System.out.println("Servlet dependencies injected successfully!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new_match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        PlayersInMatchRequest players = new PlayersInMatchRequest(
                req.getParameter("firstPlayerName"), req.getParameter("secondPlayerName")
        );

        Player firstPlayer = new Player(players.firstPlayerName());
        Player secondPlayer = new Player(players.secondPlayerName());
        Match match = Match.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
        UUID uuid = UUID.randomUUID();

        matchStorageService.createMatch(uuid, match);
        playerService.save(firstPlayer);
        playerService.save(secondPlayer);
        matchService.save(match);

        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

}
