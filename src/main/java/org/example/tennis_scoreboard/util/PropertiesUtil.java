package org.example.tennis_scoreboard.util;

import lombok.experimental.UtilityClass;
import org.example.tennis_scoreboard.exception.PropertyLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
                throw new PropertyLoadException("Properties file not found: " + PROPERTIES_FILE);
            }
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new PropertyLoadException(
                    "Failed to load properties file: %s. Error: %s".formatted(PROPERTIES_FILE, e.getMessage())
            );
        }
    }

}
