import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;



public class HelloWorldTest {
    // Ex5: Парсинг JSON
    @Test
    public void testRestAssured(){

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String answer = response.get("messages[1].message");
        System.out.println(answer);

    }
}
