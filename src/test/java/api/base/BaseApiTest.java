package api.base;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.apiBaseUrl();

        RestAssured.defaultParser = Parser.JSON;

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
