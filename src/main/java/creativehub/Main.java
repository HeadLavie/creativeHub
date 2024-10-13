package creativehub;

import files.Body;
import files.User;
import files.UserResponse;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class Main {
    public static void main(String[] args) {

        Header applicationJsonHeader = new Header("Content-Type", "application/json");
        var user = new User("11112222");
        // можно ли тут var вместо User?
        // хочу генератор пароля
        System.out.println(user.getEmail());

        RestAssured.baseURI = "https://creativehub.dev.iamrobot.xyz";
        given().header(applicationJsonHeader).body(user)
                .when().post("ch/v1/user/")
                .then().log().all().assertThat().statusCode(201).extract().response().as(UserResponse.class);
        // можно ли сделать один класс из User and UserResponse
        // извлечь данные и создать новый объект в котором одно из полей будет id или как вариант сохранить id в переменную

        String userID = UserResponse.uid;
                //положить сюда поле объекта UserResponse

        given().when().get("/ch/v1/user/" + userID)
                .then().log().all().assertThat().body("first_name", equalTo(UserResponse.first_name));

        String response = given().header(applicationJsonHeader).body(user)
                // как увидеть что именно там передается в боди?
                .when().post("ch/v1/auth/login/")
                .then().log().all().statusCode(200)
                .body("token_type", equalTo("bearer")).extract().response().asString();
        // найти в документации метод для извлечения конкретного поля


        JsonPath js = new JsonPath(response);
        String accessToken = js.getString("access_token");
        String refreshToken = js.getString("refresh_token");

        Header tokenHeader = new Header("Authorization", "Bearer " + refreshToken);

        given().log().all().header(tokenHeader)
                .when().post("ch/v1/auth/refresh/").then().assertThat().statusCode(200);
        // извлечь обновленный токен и проверить что он не равен с начальным
        // сделать все ввиде тестов


    }
}
