package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue){
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
    public static void assertJsonByName(Response Response, String name, String expectedValue){
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }
    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected");
    }
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }
    public static void assertJsonField(Response Response, String expectedFileName){
        Response.then().assertThat().body("$", hasKey(expectedFileName));
    }
    public static void assertJsonHasFields(Response Response, String [] expectedFileNames){
        for (String expectedFileName : expectedFileNames){
            Assertions.assertJsonField(Response, expectedFileName);
        }
    }
    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName) {
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }
    public static void assertJsonHasNotFields(Response Response, String [] expectedFileNames){
        for (String expectedFileName : expectedFileNames){
            Assertions.assertJsonHasNotField(Response, expectedFileName);
        }
    }
}
