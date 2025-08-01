import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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
    //Ex6: Редирект
    @Test
    public void testRestAssuredRedirect() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }
    //Ex7: Долгий редирект
    @Test
    public void testRestAssuredLongRedirect() {
        int countStep = 0;
        int statusCode = 0;
        String locationHeader = "https://playground.learnqa.ru/api/long_redirect";
        while (true){
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(locationHeader)
                    .andReturn();
            statusCode = response.getStatusCode();
            if (statusCode == 200){
                break;
            }
            countStep +=1;
            locationHeader = response.getHeader("Location");
        }
        System.out.println("Количество редиректов:"+countStep+"\nИтоговый адрес:"+locationHeader);

    }
    //Ex8: Токены
    @Test
    public void testToken() throws InterruptedException {
        String path = "https://playground.learnqa.ru/ajax/api/longtime_job";
        JsonPath response = RestAssured
                .get(path)
                .jsonPath();
        String token = response.get("token");
        System.out.println(token);
        int seconds = response.get("seconds");
        System.out.println(seconds);
        response = RestAssured
                .get(path)
                .jsonPath();
        String status = response.get("status");
        if (status != "Job is NOT ready"){
            System.out.println("The status is true");
        }else {
            System.out.println("The status is NOT true");
        }
        Thread.sleep(seconds * 1000);
        response = RestAssured
                .given()
                .queryParam("token","token")
                .get(path)
                .jsonPath();
        status = response.get("status");
        if (status != "Job is NOT ready"){
            System.out.println("The status is true");
        }else {
            System.out.println("The status is NOT true");
        }

    }
}
