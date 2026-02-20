package api.base;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.apiBaseUrl();

        // важно для AutomationExercise: парсить как JSON даже если Content-Type = text/html
        RestAssured.defaultParser = Parser.JSON;

        // логировать запрос/ответ только если тест упал
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
