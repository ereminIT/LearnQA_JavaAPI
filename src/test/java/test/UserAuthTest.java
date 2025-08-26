package test;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.ApiCoreRequests;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUer(){
        Map<String, String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api_dev/user/login", authData);
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");
        this.header = this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth, "user_id");

    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    @Story("Successful scenarios")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Regression Team")
    public void testAuthUser(){
        Response responseCheckAuth =apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api_dev/user/auth",
                        this.header,
                        this.cookie
                );

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    @Description("This test checks authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @Story("Authorization verification")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Auth Team")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    public void testNegativeAuthUser(String condition) {
        if (condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api_dev/user/auth",
                    this.cookie
            );
        } else if (condition.equals("header")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api_dev/user/auth",
                    this.header
            );
        } else {
            throw new IllegalArgumentException("Condition value is not Known :" + condition);
        }
    }
}
