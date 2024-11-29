package creativehub;

import files.Body;
import files.User;
import files.UserResponse;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;

import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Main {
    public static void main(String[] args) {

        Header applicationJsonHeader = new Header("Content-Type", "application/json");

       // System.out.println(user.getEmail()); перестало работать потому что сделала поле статичным

        RestAssured.baseURI = "https://ch-dev.testant.online";
        given().header(applicationJsonHeader).body(User.class)
                .when().post("ch/v1/user/")
                .then().log().all().assertThat().statusCode(201).extract().response().as(UserResponse.class);
        // можно ли сделать один класс из User and UserResponse
        // извлечь данные и создать новый объект в котором одно из полей будет id или как вариант сохранить id в переменную

        String userID = UserResponse.uid;
                //положить сюда поле объекта UserResponse

        given().when().get("/ch/v1/user/" + userID)
                .then().log().all().assertThat().body("first_name", equalTo(UserResponse.first_name));

        String response = given().log().all().header(applicationJsonHeader).body(User.class)
                // как увидеть что именно там передается в боди?
                .when().post("ch/v1/auth/login/")
                .then().log().all().assertThat().statusCode(200)
                .body("token_type", equalTo("bearer")).extract().response().asString();
        // найти в документации метод для извлечения конкретного поля


        JsonPath js = new JsonPath(response);
        String accessToken = js.getString("access_token");
        String refreshToken = js.getString("refresh_token");

        Header tokenHeader = new Header("Authorization", "Bearer " + refreshToken);

        given().header(tokenHeader)
                  .when().post("ch/v1/auth/refresh/").then().assertThat()
                 .statusCode(200).body("access_token", not(equalTo(accessToken)));
        // проверила что токен не равен с начальным

        given().header("Authorization", "Bearer " + accessToken).when().delete("ch/v1/user/").then()
                .assertThat().statusCode(204);
        // почему работает старый аксес токен?

        given().get("ch/v1/user/" + userID).then().assertThat()
                .statusCode(422);
         // почему в постмане я тут получаю 404?
    }
}

// update some info for the user -- delite user
// put? patch?
