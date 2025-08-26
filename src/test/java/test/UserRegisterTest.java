package test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DateGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = DateGenerator.getRegistrationData();
        userData.put("email", email);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }
    @Test
    public void testCreateUserSuccessfully() {
        String email = DateGenerator.getRandomEmail();

        Map<String, String> userData = DateGenerator.getRegistrationData();
        userData.put("email", email);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonField(responseCreateAuth, "id");
    }
    @Test
    public void testCreateUserWithInvalidEmail() {
        String email = DateGenerator.getRandomEmail().replace("@","");

        Map<String, String> userData = DateGenerator.getRegistrationData();
        userData.put("email", email);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "password", "username", "firstName", "lastName"})
    public void testCreateUserWithoutRequiredField(String fieldName) {
        Map<String, String> userData = DateGenerator.getRegistrationData();
        userData.remove(fieldName);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: " + fieldName);
    }

    @Test
    public void testCreateUserWithShortName() {
        Map<String, String> userData = DateGenerator.getRegistrationData();
        userData.put("username", "A");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    public void testCreateUserWithVeryLongName() {
        Map<String, String> userData = DateGenerator.getRegistrationData();
        String longName = "A".repeat(251);
        userData.put("username", longName);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/", userData);

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }
}
