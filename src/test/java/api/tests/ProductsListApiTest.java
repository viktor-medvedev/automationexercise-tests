package api.tests;

import api.base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ProductsListApiTest extends BaseApiTest {

    @Test
    public void productsListReturns200() {
        int status =
                given()
                        .when()
                        .get("/api/productsList")
                        .then()
                        .extract().statusCode();

        Assert.assertEquals(status, 200);
    }
}
