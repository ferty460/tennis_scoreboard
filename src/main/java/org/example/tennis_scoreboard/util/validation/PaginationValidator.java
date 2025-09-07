package org.example.tennis_scoreboard.util.validation;

import org.example.tennis_scoreboard.context.Component;
import org.example.tennis_scoreboard.exception.PaginationException;

@Component("paginationValidator")
public class PaginationValidator implements Validator<String, PaginationException> {

    @Override
    public void validate(String page) throws PaginationException {
        if (page != null && !page.matches("[0-9]+")) {
            throw new PaginationException("Wrong page format");
        }
    }

}
