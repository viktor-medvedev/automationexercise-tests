package api.base;

import config.TestConfig;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.apiBaseUrl();
    }
}