package api.tests;

import api.base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProductsApiTest extends BaseApiTest {

    @Test
    public void postProductsListReturns405() {
        var jp =
                given()
                        .when()
                        .post("/api/productsList")
                        .then()
                        .statusCode(200) // HTTP может быть 200
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 405);
        Assert.assertTrue(jp.getString("message")
                        .contains("This request method is not supported"),
                "Unexpected message: " + jp.getString("message"));
    }
}
