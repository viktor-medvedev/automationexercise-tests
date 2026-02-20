package api.tests;

import api.base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProductsListApiTest extends BaseApiTest {

    @Test
    public void productsListReturns200() {
        var jp =
                given()
                        .when()
                        .get("/api/productsList")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 200);

        List<Map<String, Object>> products = jp.getList("products");
        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() > 0, "Products list is empty");

        Map<String, Object> first = products.get(0);
        Assert.assertNotNull(first.get("id"));
        Assert.assertNotNull(first.get("name"));
        Assert.assertNotNull(first.get("price"));
        Assert.assertNotNull(first.get("brand"));
        Assert.assertNotNull(first.get("category"));
    }
}
