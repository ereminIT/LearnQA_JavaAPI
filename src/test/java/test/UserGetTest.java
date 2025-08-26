package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import lib.ApiCoreRequests;


public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api_dev/user/2")
                .andReturn();
        Assertions.assertJsonField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }
    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth= apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api_dev/user/login",
                authData
        );

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api_dev/user/2",
                header,
                cookie
        );

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
    @Test
    public void testGetUserDetailsAuthAsDifferentUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth= apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api_dev/user/login",
                authData
        );

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api_dev/user/1",
                header,
                cookie
        );

        Assertions.assertJsonField(responseUserData, "username");
        String[] expectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
    }
}
