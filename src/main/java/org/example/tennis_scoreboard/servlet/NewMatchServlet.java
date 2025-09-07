package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.dto.MatchDto;
import org.example.tennis_scoreboard.dto.PlayerDto;
import org.example.tennis_scoreboard.dto.PlayersInMatchRequest;
import org.example.tennis_scoreboard.exception.PlayerNameException;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.service.MatchStorageService;
import org.example.tennis_scoreboard.service.PlayerService;
import org.example.tennis_scoreboard.util.validation.Validator;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends InjectableHttpServlet {

    private MatchStorageService matchStorageService;
    private MatchService matchService;
    private PlayerService playerService;

    private Validator<PlayersInMatchRequest, PlayerNameException> validator;

    @Override
    public void init() {
        super.init();
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        this.validator = context.getBean("playerNameValidator");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/new_match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlayersInMatchRequest players = new PlayersInMatchRequest(
                req.getParameter("firstPlayerName"), req.getParameter("secondPlayerName")
        );
        validator.validate(players);

        PlayerDto firstPlayerDto = new PlayerDto(null, players.firstPlayerName());
        PlayerDto secondPlayerDto = new PlayerDto(null, players.secondPlayerName());
        firstPlayerDto = playerService.save(firstPlayerDto);
        secondPlayerDto = playerService.save(secondPlayerDto);

        MatchDto matchDto = new MatchDto(null, firstPlayerDto, secondPlayerDto, null);
        UUID uuid = UUID.randomUUID();

        matchDto = matchService.save(matchDto);
        matchStorageService.createMatch(uuid, matchDto);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + uuid);
    }

}
