package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.dto.PlayersInMatchRequest;
import org.example.tennis_scoreboard.model.Match;
import org.example.tennis_scoreboard.model.Player;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;
import org.example.tennis_scoreboard.service.PlayerService;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@WebServlet("/new-match")
public class NewMatchServlet extends InjectableHttpServlet {

    private MatchStorageService matchStorageService;
    private MatchService matchService;
    private PlayerService playerService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new_match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlayersInMatchRequest players = new PlayersInMatchRequest(
                req.getParameter("firstPlayerName"), req.getParameter("secondPlayerName")
        );

        Player firstPlayer = new Player(players.firstPlayerName());
        Player secondPlayer = new Player(players.secondPlayerName());
        firstPlayer = playerService.save(firstPlayer);
        secondPlayer = playerService.save(secondPlayer);

        Match match = Match.builder()
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
        UUID uuid = UUID.randomUUID();

        matchService.save(match);
        matchStorageService.createMatch(uuid, match);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + uuid);
    }

}
