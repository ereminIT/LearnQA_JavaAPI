package test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import lib.ApiCoreRequests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);
        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid"),
                editData
        );


        //GET
        Response responseUserData = apiCoreRequests.makeGetRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                this.getHeader(responseGetAuth, "x-csrf-token"),
                this.getCookie(responseGetAuth, "auth_sid")
        );

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @DisplayName("Attempt to change user data without authorization")
    public void testEditUserWithoutAuthorization() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api_dev/user/",
                userData
        );

        String userId = responseCreateAuth.jsonPath().getString("id");

        // EDIT
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "New Name");

        Response responseEdit = apiCoreRequests.makePostRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                editData
        );

        Assertions.assertResponseCodeEquals(responseEdit, 400);

    }

    @Test
    @DisplayName("Attempt to change user data by another user")
    public void testEditUserWithDifferentUser() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);

        // EDIT
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "New Name");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api_dev/user/1",
                header,
                cookie,
                editData
        );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);

    }

    @Test
    @DisplayName("Attempting to change a user's email while logged in as the same user to a new email without the @ symbol")
    public void testEditUserInvalidEmail() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);

        //EDIT
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        String email = DateGenerator.getRandomEmail().replace("@","");
        editData.put("email", email);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                header,
                cookie,
                editData
        );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

    @Test
    @DisplayName("Attempting to change the user's firstName while being logged in as the same user, to a very short value of one character")
    public void testEditUserWithTooShortFirstName() {
        //GENERATE USER
        Map<String, String> userData = DateGenerator.getRegistrationData();
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);
        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);

        //EDIT
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "A");

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api_dev/user/" + userId,
                header,
                cookie,
                editData
        );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
    }

}
