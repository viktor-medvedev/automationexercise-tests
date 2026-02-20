package api.tests;

import api.base.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BrandsApiTest extends BaseApiTest {

    @Test
    public void getBrandsListReturns200() {
        var jp =
                given()
                        .when()
                        .get("/api/brandsList")
                        .then()
                        .statusCode(200)   // HTTP часто 200
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 200);

        List<Map<String, Object>> brands = jp.getList("brands");
        Assert.assertNotNull(brands, "brands is null");
        Assert.assertTrue(brands.size() > 0, "brands list is empty");

        Map<String, Object> first = brands.get(0);
        Assert.assertNotNull(first.get("id"));
        Assert.assertNotNull(first.get("brand"));
    }

    @Test
    public void putBrandsListReturns405() {
        var jp =
                given()
                        .when()
                        .put("/api/brandsList")
                        .then()
                        .statusCode(200)   // HTTP может быть 200, а код внутри responseCode
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 405);
        Assert.assertTrue(jp.getString("message").contains("This request method is not supported"),
                "Unexpected message: " + jp.getString("message"));
    }
}
