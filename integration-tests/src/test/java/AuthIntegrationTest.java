import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class AuthIntegrationTest {

    @BeforeAll
    static void setUp(){
        RestAssured.baseURI = "http://localhost:4004";

    }

    @Test
    public void shouldReturnOkValidToken(){

        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "password123"
                
                }
                """;

        Response rs = given().
        contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        Logger.getGlobal().info(rs.getBody().toString());
//        System.out.println("generated token " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorizedWhenInvalidCredential(){

        String loginPayload = """
                {
                    "email": "testuser@test.com",
                    "password": "123123"
                
                }
                """;

       Response rs = given().
                contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
               .extract().response();

        Logger.getGlobal().info(rs.getBody().toString());
//        System.out.println("generated token " + response.jsonPath().getString("token"));
    }
}
