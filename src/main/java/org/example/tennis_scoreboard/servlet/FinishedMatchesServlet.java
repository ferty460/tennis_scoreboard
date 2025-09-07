package org.example.tennis_scoreboard.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.tennis_scoreboard.context.ApplicationContext;
import org.example.tennis_scoreboard.dto.FinishedMatchDto;
import org.example.tennis_scoreboard.exception.PaginationException;
import org.example.tennis_scoreboard.model.PaginationResult;
import org.example.tennis_scoreboard.service.MatchService;
import org.example.tennis_scoreboard.util.validation.Validator;

import java.io.IOException;

@WebServlet("/matches")
public class FinishedMatchesServlet extends InjectableHttpServlet {

    private MatchService matchService;

    private Validator<String, PaginationException> validator;

    @Override
    public void init() {
        super.init();
        ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("applicationContext");
        this.validator = context.getBean("paginationValidator");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String playerName = req.getParameter("filter_by_player_name");
        validator.validate(page);

        PaginationResult<FinishedMatchDto> result = matchService.getAllFinishedMatches(page, playerName);

        req.setAttribute("matches", result.getItems());
        req.setAttribute("pagination", result);
        req.setAttribute("currentFilter", playerName);

        req.getRequestDispatcher("WEB-INF/finished_matches.jsp").forward(req, resp);
    }

}
