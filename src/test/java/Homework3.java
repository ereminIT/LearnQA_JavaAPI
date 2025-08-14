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

    //Ex13: User Agent
    public static Stream<Arguments> userAgentDataProvider() {
        return Stream.of(
                Arguments.of("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30", "Mobile", "No", "Android"),
                Arguments.of("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1", "Mobile", "Chrome", "iOS"),
                Arguments.of("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)", "Googlebot", "Unknown", "Unknown"),
                Arguments.of("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0", "Web", "Chrome", "No"),
                Arguments.of("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1", "Mobile", "No", "iPhone")
        );
    }

    @ParameterizedTest
    @MethodSource("userAgentDataProvider")
    public void testUserAgent(String userAgent, String expectedPlatform, String expectedBrowser ,String expectedDevice) {
        Response response = given()
                .header("User-Agent", userAgent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        String device = response.jsonPath().getString("device");
        String browser = response.jsonPath().getString("browser");
        String platform = response.jsonPath().getString("platform");

        assertEquals(expectedDevice, device, "Device mismatch for user agent");
        assertEquals(expectedBrowser, browser, "Browser mismatch for user agent");
        assertEquals(expectedPlatform, platform, "Platform mismatch for user agent");
    }
}
