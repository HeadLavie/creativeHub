package creativehub;

import files.body;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Main {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://creativehub.dev.iamrobot.xyz";
        given().header("Content-Type", "application/json").body(body.addBody())
                .when().post("ch/v1/user/")
                .then().log().all().assertThat().statusCode(400);

        given().when().get("/ch/v1/user/95e94649-fc22-400d-aadd-8fb2ba43f489")
                .then().log().all().assertThat().body("first_name", equalTo("nadya"));

        String response = given().header("Content-Type", "application/json").body("{\n" +
                        "  \"email\": \"lala@google.com\",\n" +
                        "  \"password\": \"11112222\"\n" +
                        "}")
                .when().post("ch/v1/auth/login/")
                .then().log().all().statusCode(200)
                .body("token_type", equalTo("bearer")).extract().response().asString();

        JsonPath js = new JsonPath(response);
        String accessToken = js.getString("access_token");
        String refreshToken = js.getString("refresh_token");

        given().log().all().header("Authorization", "Bearer " + refreshToken)
                .when().post("ch/v1/auth/refresh/").then().assertThat().statusCode(200);

    }


}
