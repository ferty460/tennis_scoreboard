package org.example.tennis_scoreboard.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.exception.NotFoundException;
import org.example.tennis_scoreboard.exception.PaginationException;
import org.example.tennis_scoreboard.exception.PlayerNameException;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class ErrorHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (PlayerNameException | PaginationException e) {
            sendError(res, req, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), "WEB-INF/new_match.jsp");
        } catch (NotFoundException e) {
            sendError(res, req, HttpServletResponse.SC_NOT_FOUND, e.getMessage(), "/error.jsp");
        } catch (Exception e) {
            sendError(res, req, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), "/error.jsp");
        }
    }

    private void sendError(HttpServletResponse res, HttpServletRequest req, int status, String message, String path)
            throws ServletException, IOException {
        log.error(message);
        req.setAttribute("errorMessage", message);
        req.setAttribute("statusCode", status);
        res.setStatus(status);
        req.getRequestDispatcher(path).forward(req, res);
    }

}
