import files.User;
import files.UserResponse;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SampleTest {

    private static Header applicationJsonHeader;
    private static User user;
    private static String accessToken;  // Class-level variable for access token
    private static String refreshToken; // Class-level variable for refresh token
    private static Header tokenHeader;

    @BeforeAll
    static void setUp() {
        applicationJsonHeader = new Header("Content-Type", "application/json");
        user = new User();

        RestAssured.baseURI = "https://ch-dev.testant.online";
    }


    @Test
    @Order(1)
    void shouldPostUser() {

        given().log().all().header(applicationJsonHeader)

                // можно ли тут Header.class("Content-Type", "application/json")

                .body(user).when().post("ch/v1/user/")

                // body(class.User) не сработало
                .then().assertThat().statusCode(201).extract().response().as(UserResponse.class);

    }

    @Test
    @Order(2)
    void shouldGetUser() {

        given().when().get("/ch/v1/user/" + UserResponse.uid)
                .then().log().all().assertThat().body("first_name", equalTo(UserResponse.first_name));
    }

    @Test
    @Order(3)
    void shouldAuthUser() {

        String response = given().header(applicationJsonHeader).body("{\n" +
                        "  \"email\": \"" + user.getEmail() + "\",\n" +
                        "  \"password\": \"" + user.getPassword() + "\"\n" +
                        "}")
                // какие есть способы закодить лучше

                .when().post("ch/v1/auth/login/")
                .then().statusCode(200).body("token_type", equalTo("bearer"))
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        accessToken = js.getString("access_token");
        refreshToken = js.getString("refresh_token");
        tokenHeader = new Header("Authorization", "Bearer " + refreshToken);
    }

    @Test
    @Order(4)
    void shouldRefreshToken() {

        String response = given().header(tokenHeader)
                .when().post("ch/v1/auth/refresh/").then().assertThat()
                .statusCode(200).body("access_token", not(equalTo(accessToken)))
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        accessToken = js.getString("access_token");

        // повторяются два теста

    }

    @Test
    @Order(5)
    void shouldDeleteUser() {

        given().header("Authorization", "Bearer " + accessToken).when().delete("ch/v1/user/").then()
                .assertThat().statusCode(204);

    }
}