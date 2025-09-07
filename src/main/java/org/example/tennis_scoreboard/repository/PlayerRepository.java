package org.example.tennis_scoreboard.repository;

import org.example.tennis_scoreboard.model.Player;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Long, Player> {

    Optional<Player> findByName(String name);

}
