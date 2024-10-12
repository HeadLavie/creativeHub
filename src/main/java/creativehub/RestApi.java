package creativehub;

import com.google.gson.Gson;
import files.ValueHelper;
import files.Body;

import java.net.URI;
import java.net.http.HttpRequest;

import static groovy.json.JsonOutput.toJson;

public class RestApi {
    public static void main(String[] args) throws Exception {

        ValueHelper valueHelper = new ValueHelper();
        valueHelper.setFirst_name("Nick");
        valueHelper.setSecond_name("Petrov");
        valueHelper.setEmail("petrov@gmail.com");
        valueHelper.setPassword("11112222");

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(valueHelper);

        System.out.println(jsonRequest);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(new URI("https://creativehub.dev.iamrobot.xyz"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Body.addBody())
                        .build();

    }
}
