package test;

import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.ApiCoreRequests;
import lib.DateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @DisplayName("Attempt to delete a user with ID 2")
    public void testDeleteProtectedUser() {
        // User data
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Login
        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        // Delete
        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/",
                "2",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertResponseTextEqualsJson(responseDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");


        // Get
        responseGetAuth= apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);
    }

    @Test
    @DisplayName("Successful deletion of the created user")
    public void testDeleteCreatedUserSuccessfully() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        // Delete
        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/",
                userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseCodeEquals(responseDelete, 200);

        // Get
        responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );
        Assertions.assertResponseCodeEquals(responseGetAuth, 400);
    }

    @Test
    @DisplayName("Аttempting to delete a user while being authorized by another user")
    public void testDeleteUserWithDifferentAuth() {
        // User data
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        // Login
        Response responseGetAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );

        // Delete
        Response responseDelete = apiCoreRequests.deleteUser(
                "https://playground.learnqa.ru/api/user/",
                "1",
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );
        Assertions.assertResponseCodeEquals(responseDelete, 400);
        Assertions.assertResponseTextEqualsJson(responseDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

        // Get
        responseGetAuth= apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
        );
        Assertions.assertResponseCodeEquals(responseGetAuth, 200);
    }

}
