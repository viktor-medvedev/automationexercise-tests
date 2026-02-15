package config;

public class TestConfig {
    public static String baseUrl() {
        return System.getProperty("BASE_URL", "https://automationexercise.com");
    }

    public static String apiBaseUrl() {
        return System.getProperty("API_BASE_URL", "https://automationexercise.com");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("HEADLESS", "true"));
    }
}