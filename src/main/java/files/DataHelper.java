package files;


import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {

    public static PostUser getPostUser(){

        var faker = new Faker();
        var first_name = faker.name().firstName();
        var second_name = faker.name().lastName();
        var email = faker.internet().emailAddress();
        String password = "22223333";
        return new PostUser(first_name, second_name, email, password);
        // почему не создается конструктор
    }

    @Value
    public static class PostUser {

        private String first_name;
        private String second_name;
        private String email;
        private String password;

    }

    @Value
    public static class GetUser {

        public String first_name;
        public String second_name;
        public String email;
        public String uid;
        public String username;
        public String photo;
        public String last_visited_at;
    }

}
