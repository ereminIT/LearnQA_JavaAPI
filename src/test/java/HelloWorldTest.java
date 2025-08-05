import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


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
    // ex:9 Подбор пароля
    @Test
    public void testPasswordSelection (){

        String[] passwords= {"123456","qwerty","password","123456789","1234567","12345678","12345","iloveyou","111111","123123","abc123","qwerty123","1q2w3e4r","admin","qwertyuiop","654321","555555","lovely","7777777","welcome","888888","princess","dragon","password1","123qwe"};
        Map<String, String> cookies = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        data.put("login","super_admin");
        int i = 0;
        String resAuth = "";
        String responseCookie = "";
        do{
            data.put("password",passwords[i]);
            Response responseForGet = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            responseCookie = responseForGet.getCookie("auth_cookie");
            cookies.put("auth_cookie",responseCookie);

            Response responseForCheck = RestAssured
                    .given()
                    .body(data)
                    .cookies(cookies)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();
            resAuth = responseForCheck.getBody().asString();

            if (resAuth.equals("You are authorized")){
                break;
            }
            i++;
        } while (true);
        System.out.println("Password found: "+passwords[i]);
    }

}
