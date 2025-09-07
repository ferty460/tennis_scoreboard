package org.example.tennis_scoreboard.util.validation;

public interface Validator<T, E extends RuntimeException> {

    void validate(T object) throws E;

}
