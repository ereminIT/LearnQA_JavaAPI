import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework3 {
    //Ex11: Тест запроса на метод cookie
    @Test
    public void HomeworkCookie(){
        Response responseGetCookie = RestAssured
                .given()
                .post("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String,String> cookies = responseGetCookie.cookies();
        //System.out.println(cookies);
        assertTrue(cookies.containsKey("HomeWork"),"Response doesn't have 'HomeWork' cookie");
    }

    //Ex12: Тест запроса на метод header
    @Test
    public void HomeworkHeader(){
        Response responseGetHeader = RestAssured
                .given()
                .post("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Headers headers = responseGetHeader.getHeaders();
        //System.out.println(headers);
        assertTrue(headers.hasHeaderWithName("Content-Type"), "Response doesn't have 'Content-Type' cookie");
    }


}
