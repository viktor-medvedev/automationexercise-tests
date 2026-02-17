package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (is == null) {
                throw new IllegalStateException("application.properties not found in src/test/resources");
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) return sys;

        String val = PROPS.getProperty(key);
        if (val == null) throw new IllegalArgumentException("Missing property: " + key);
        return val;
    }


    public static String getOrDefault(String key, String defaultValue) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) return sys;

        return PROPS.getProperty(key, defaultValue);
    }

    public static String rawFromFile(String key) {
        return PROPS.getProperty(key);
    }

}
