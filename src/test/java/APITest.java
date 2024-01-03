import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

public class APITest {

    private static final String BASE_URI = "http://localhost:7081/api/books/";
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void testAdminCreateNewBookSuccessfully() {


        // Define the request body with valid book data
        String requestBody = "{\"title\": \"New Book Title\", \"author\": \"Author Name\"}";


        given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post()  // Use POST to create a new book
                .then()
                .assertThat()
                .statusCode(201)
                .body("title", equalTo("New Book Title"))
                .body("author", equalTo("Author Name"));
    }
    @Test
    void test() {
        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .get(); // You can append the endpoint to the base URL here
        Assert.assertEquals(200, response.getStatusCode());
    }

    //Update an existing book with valid data
    @Test
    public void test1() {

        String baseUri = "http://localhost:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";

        try {
            Response response = given()
                    .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .when()
                    .put("/1");

            Assert.assertEquals(200, response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    //Update book without authorization
    @Test
    void test2() {
        String baseUri = "http://localhost:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .put(baseUri);

        Assert.assertEquals(401, response.getStatusCode());
    }


    //Update a book with wrong authorization
    @Test
    void test3() {
        String baseUri = "http://localhost:7081/api/books/1"; // Update the URL with the correct resource ID

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic("admin", "Password")
                .header("Content-Type", "application/json")
                .body(requestBody) // Set the request body
                .when()
                .put(baseUri);
        Assert.assertEquals(401, response.getStatusCode());

    }

    //Update a book with user authorization
    @Test
    void test4() {
        String baseUri = "http://localhost:7081/api/books/1"; // Update the URL with the correct resource ID

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic("user", "password")
                .header("Content-Type", "application/json")
                .body(requestBody) // Set the request body
                .when()
                .put(baseUri);
        Assert.assertEquals(403, response.getStatusCode());
                 // Log the response for debugging purposes
    }

    //Update a book with invalid book ID
    @Test
    void test5() {
        String baseUri = "http://localhost:7081/api/books/3";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic("admin", "password")
                .header("Content-Type", "application/json")
                .body(requestBody) // Set the request body
                .when()
                .put(baseUri);
        Assert.assertEquals(404, response.getStatusCode());

    }

    //Update a book with missing title
    @Test
    void test6() {
        String baseUri = "http://localhost:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic("admin", "password")
                .header("Content-Type", "application/json")
                .body(requestBody) // Set the request body
                .when()
                .put(baseUri);
        Assert.assertEquals(400, response.getStatusCode());

    }

    //Update a book with missing author
    @Test
    void test7() {
        String baseUri = "http://localhost:7081/api/books/1";

        String requestBody = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"The World: A Family History\",\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic("admin", "password")
                .header("Content-Type", "application/json")
                .body(requestBody) // Set the request body
                .when()
                .put(baseUri);
        Assert.assertEquals(400, response.getStatusCode());
    }

}
