package api.tests;

import api.base.BaseApiTest;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class VerifyLoginApiTest extends BaseApiTest {

    private String email;
    private String password;

    @BeforeClass
    public void createUserForPositiveLogin() {
        email = "user_" + UUID.randomUUID() + "@mail.com";
        password = "Pass12345";

        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("name", "Viktor")
                        .formParam("email", email)
                        .formParam("password", password)
                        .formParam("title", "Mr")
                        .formParam("birth_date", "10")
                        .formParam("birth_month", "1")
                        .formParam("birth_year", "1990")
                        .formParam("firstname", "Viktor")
                        .formParam("lastname", "Medvedev")
                        .formParam("company", "QA")
                        .formParam("address1", "Street 1")
                        .formParam("address2", "Apt 2")
                        .formParam("country", "United States")
                        .formParam("zipcode", "12345")
                        .formParam("state", "FL")
                        .formParam("city", "Niceville")
                        .formParam("mobile_number", "1234567890")
                        .when()
                        .post("/api/createAccount")
                        .then()
                        .statusCode(anyOf(is(200), is(201))) // у них бывает по-разному
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 201, "Create account failed: " + jp.getString("message"));
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        if (email == null) return;

        given()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .delete("/api/deleteAccount")
                .then()
                .statusCode(anyOf(is(200), is(201)));
    }

    @Test
    public void verifyLoginValidReturns200() {
        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("email", email)
                        .formParam("password", password)
                        .when()
                        .post("/api/verifyLogin")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 200);
        Assert.assertTrue(jp.getString("message").contains("User exists"), "Unexpected message: " + jp.getString("message"));
    }

    @Test
    public void verifyLoginWithoutEmailReturns400() {
        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("password", "whatever")
                        .when()
                        .post("/api/verifyLogin")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 400);
        Assert.assertTrue(jp.getString("message").toLowerCase().contains("missing"), "Unexpected message: " + jp.getString("message"));
    }

    @Test
    public void verifyLoginInvalidReturns404() {
        var jp =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("email", "nope_" + UUID.randomUUID() + "@mail.com")
                        .formParam("password", "badpass")
                        .when()
                        .post("/api/verifyLogin")
                        .then()
                        .statusCode(200)
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 404);
        Assert.assertTrue(jp.getString("message").contains("User not found"), "Unexpected message: " + jp.getString("message"));
    }

    @Test
    public void deleteVerifyLoginReturns405() {
        var jp =
                given()
                        .when()
                        .delete("/api/verifyLogin")
                        .then()
                        .statusCode(anyOf(is(200), is(405))) // на всякий
                        .extract().jsonPath();

        Assert.assertEquals(jp.getInt("responseCode"), 405);
        Assert.assertTrue(jp.getString("message").contains("not supported"), "Unexpected message: " + jp.getString("message"));
    }
}
