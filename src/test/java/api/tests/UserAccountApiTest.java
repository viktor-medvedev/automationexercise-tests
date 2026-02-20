package api.tests;

import api.base.BaseApiTest;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class UserAccountApiTest extends BaseApiTest {

    @Test
    public void userLifecycle_create_get_update_get_delete() {
        String email = "user_" + UUID.randomUUID() + "@mail.com";
        String password = "Pass12345";

        Map<String, String> user = new HashMap<>();
        user.put("name", "Viktor");
        user.put("email", email);
        user.put("password", password);
        user.put("title", "Mr");
        user.put("birth_date", "10");
        user.put("birth_month", "1");
        user.put("birth_year", "1990");
        user.put("firstname", "Viktor");
        user.put("lastname", "Medvedev");
        user.put("company", "QA");
        user.put("address1", "Street 1");
        user.put("address2", "Apt 2");
        user.put("country", "United States");
        user.put("zipcode", "12345");
        user.put("state", "FL");
        user.put("city", "Niceville");
        user.put("mobile_number", "1234567890");

        try {
            // 11) createAccount -> responseCode 201
            var create =
                    given()
                            .contentType(ContentType.URLENC)
                            .formParams(user)
                            .when()
                            .post("/api/createAccount")
                            .then()
                            .statusCode(anyOf(is(200), is(201))) // у них HTTP иногда плавает
                            .extract().jsonPath();

            Assert.assertEquals(create.getInt("responseCode"), 201,
                    "Create failed: " + create.getString("message"));

            // 13) getUserDetailByEmail -> responseCode 200
            var get1 =
                    given()
                            .queryParam("email", email)
                            .when()
                            .get("/api/getUserDetailByEmail")
                            .then()
                            .statusCode(200)
                            .extract().jsonPath();

            Assert.assertEquals(get1.getInt("responseCode"), 200);
            // мягкие проверки на соответствие
            Assert.assertTrue(get1.getString("user.email").equalsIgnoreCase(email),
                    "Email mismatch in user detail");

            // 12) updateAccount -> responseCode 200
            user.put("city", "Destin");
            var upd =
                    given()
                            .contentType(ContentType.URLENC)
                            .formParams(user)
                            .when()
                            .put("/api/updateAccount")
                            .then()
                            .statusCode(200)
                            .extract().jsonPath();

            Assert.assertEquals(upd.getInt("responseCode"), 200,
                    "Update failed: " + upd.getString("message"));

            // 13) getUserDetailByEmail again -> убедимся, что обновилось (если поле возвращается)
            var get2 =
                    given()
                            .queryParam("email", email)
                            .when()
                            .get("/api/getUserDetailByEmail")
                            .then()
                            .statusCode(200)
                            .extract().jsonPath();

            Assert.assertEquals(get2.getInt("responseCode"), 200);

            String city = get2.getString("user.city");
            if (city != null) { // на случай если API не возвращает city
                Assert.assertEquals(city, "Destin");
            }

        } finally {
            // 14) deleteAccount -> responseCode 200 (cleanup обязателен)
            var del =
                    given()
                            .contentType(ContentType.URLENC)
                            .formParam("email", email)
                            .formParam("password", password)
                            .when()
                            .delete("/api/deleteAccount")
                            .then()
                            .statusCode(200)
                            .extract().jsonPath();

            Assert.assertEquals(del.getInt("responseCode"), 200,
                    "Delete failed: " + del.getString("message"));
        }
    }
}
