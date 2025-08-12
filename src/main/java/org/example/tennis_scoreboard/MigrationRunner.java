package org.example.tennis_scoreboard;

import org.example.tennis_scoreboard.util.PropertiesUtil;
import org.flywaydb.core.Flyway;

public class MigrationRunner {

    public static final String DB_URL_PARAM = "db.url";

    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(PropertiesUtil.getProperty(DB_URL_PARAM), null, null)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
    }

}
