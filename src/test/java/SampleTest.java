import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class SampleTest {

    @Test
    void shouldPostUser() {

        given().baseUri("https://creativehub.dev.iamrobot.xyz")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "  \"first_name\": \"nadya\",\n" +
                        "  \"second_name\": \"lalala\",\n" +
                        "  \"email\": \"lalala@google.com\",\n" +
                        "  \"password\": \"11112222\"\n" +
                        "}").when().post("ch/v1/user/")
                .then().log().all().statusCode(400);
    }

    @Test
    void shouldGetUser() {

        given().baseUri("https://creativehub.dev.iamrobot.xyz/ch/v1/user")
                .when().get("95e94649-fc22-400d-aadd-8fb2ba43f489/")
                .then().statusCode(200).contentType("application/json");
    }

    @Test
    void shouldAuthUser() {

        given().header("Content-Type", "application/json").baseUri("https://creativehub.dev.iamrobot.xyz").body("{\n" +
                        "  \"email\": \"lala@google.com\",\n" +
                        "  \"password\": \"11112222\"\n" +
                        "}")
                .when().post("ch/v1/auth/login/")
                .then().statusCode(200).body("token_type", equalTo("bearer"));
    }
}