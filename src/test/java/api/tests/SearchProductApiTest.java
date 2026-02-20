package api.tests;

import api.base.BaseApiTest;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class SearchProductApiTest extends BaseApiTest {

    @Test
    public void searchProductWithParamReturns200() {
        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("search_product", "top")
                        .when()
                        .post("/api/searchProduct")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 200);

        List<Map<String, Object>> products = jp.getList("products");
        Assert.assertNotNull(products, "products is null");
        Assert.assertTrue(products.size() > 0, "No products returned for search");

        // мягкая контрактная проверка на первый элемент
        Map<String, Object> first = products.get(0);
        Assert.assertNotNull(first.get("id"));
        Assert.assertNotNull(first.get("name"));
    }

    @Test
    public void searchProductWithoutParamReturns400() {
        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .when()
                        .post("/api/searchProduct")
                        .then()
                        .statusCode(200) // HTTP может быть 200
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 400);
        Assert.assertTrue(
                jp.getString("message").toLowerCase().contains("search_product") &&
                        jp.getString("message").toLowerCase().contains("missing"),
                "Unexpected message: " + jp.getString("message")
        );
    }
}
