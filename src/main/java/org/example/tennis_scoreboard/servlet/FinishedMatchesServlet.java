package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.dto.FinishedMatchDto;
import org.example.tennis_scoreboard.model.PaginationResult;
import org.example.tennis_scoreboard.service.MatchService;

import java.io.IOException;

@Slf4j
@WebServlet("/matches")
public class FinishedMatchesServlet extends InjectableHttpServlet {

    private MatchService matchService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");

        PaginationResult<FinishedMatchDto> result = matchService.getAllFinishedMatches(page, playerName);

        req.setAttribute("matches", result.getItems());
        req.setAttribute("pagination", result);
        req.setAttribute("currentFilter", playerName);

        req.getRequestDispatcher("finished_matches.jsp").forward(req, resp);
    }

}
