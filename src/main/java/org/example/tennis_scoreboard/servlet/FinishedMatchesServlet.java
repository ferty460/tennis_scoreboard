package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.service.MatchService;

import java.io.IOException;

@Slf4j
@WebServlet("/matches")
public class FinishedMatchesServlet extends InjectableHttpServlet {

    private MatchService matchService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("matches", matchService.getAllFinishedMatches());
        req.getRequestDispatcher("finished_matches.jsp").forward(req, resp);
    }

}
