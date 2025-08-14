import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Homework3 {
    //Ex11: Тест запроса на метод cookie
    @Test
    public void HomeworkCookie(){
        Response responseGetCookie = given()
                .post("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Map<String,String> cookies = responseGetCookie.cookies();
        //System.out.println(cookies);
        assertTrue(cookies.containsKey("HomeWork"),"Response doesn't have 'HomeWork' cookie");
    }

    //Ex12: Тест запроса на метод header
    @Test
    public void HomeworkHeader(){
        Response responseGetHeader = given()
                .post("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        Headers headers = responseGetHeader.getHeaders();
        //System.out.println(headers);
        assertTrue(headers.hasHeaderWithName("Content-Type"), "Response doesn't have 'Content-Type' cookie");
    }


}
