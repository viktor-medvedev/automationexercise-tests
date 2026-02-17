package utils;

public class TestData {

    public static String uniqueEmail() {
        return "viktor.qa+" + System.currentTimeMillis() + "@mailinator.com";
    }

    public static String password() {
        return "TestPass123!";
    }

    public static String name() {
        return "Viktor";
    }
}
