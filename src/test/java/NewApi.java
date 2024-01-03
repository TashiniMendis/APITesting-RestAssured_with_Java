import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
//import org.junit.Test;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewApi {

    @Test
    public void testPutBook() {
        // Create a RequestSpecification object
        RequestSpecification request = RestAssured.given()
                .auth().preemptive().basic("admin", "password")
                .baseUri("http://localhost:7081")
                .header("Content-Type", "application/json");

        // Set the request body
        String requestBody = "{\n" +
                "  \"id\": 1,\n" +
                "  \"title\": \"The Lord of the Rings\",\n" +
                "  \"author\": \"J.R.R. Tolkien\"\n" +
                "}";

        // Make the PUT request and get the response
        Response response = request.put("/api/books/1", requestBody);

        // Validate the response status code
        assertThat(response.statusCode(), equalTo(200));

        // Validate the response header
        assertThat(response.getHeader("Content-Type"), equalTo("application/json"));

        // Validate the response body
//        String responseBody = response.getBody().asString();
//        assertThat(responseBody, contains("\"title\": \"The Lord of the Rings\""));
    }
}
