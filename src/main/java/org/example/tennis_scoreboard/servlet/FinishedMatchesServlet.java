package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.service.MatchService;

import java.io.IOException;

@WebServlet("/matches")
public class FinishedMatchesServlet extends HttpServlet {

    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        if (context == null) {
            throw new ServletException("ApplicationContext is null!");
        }

        this.matchService = context.getBean(MatchService.class);

        // log:
        System.out.println("[FinishedMatchesServlet] Servlet dependencies injected successfully!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("matches", matchService.getAllFinishedMatches());
        req.getRequestDispatcher("finished_matches.jsp").forward(req, resp);
    }

}
