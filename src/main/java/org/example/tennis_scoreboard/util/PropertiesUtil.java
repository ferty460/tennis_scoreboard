package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.tennis_scoreboard.exception.PropertyLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@UtilityClass
public class PropertiesUtil {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                log.error("Properties file not found: {}", PROPERTIES_FILE);
                throw new PropertyLoadException("Properties file not found: " + PROPERTIES_FILE);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            log.error("Could not load properties file: {}", PROPERTIES_FILE);
            throw new PropertyLoadException("Failed to load properties file: " + PROPERTIES_FILE +
                    ". Error: " + e.getMessage());
        }
    }

}
