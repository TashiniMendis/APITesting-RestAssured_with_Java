import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class UpdateBookTest {

    private static final String BASE_URI = "http://127.0.0.1:7081/api/books/";
    private static final String ADMIN_USERNAME = "admin";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";

    private static final int bookId = 1;

    private static final String title = "The World: A Family History";
    private static final String author = "British historian Simon Sebag Montefiore";

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void testAdminCreateNewBookSuccessfully() {

        String requestBody = "{\"title\": \"New Book Title\", \"author\": \"Author Name\"}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post();

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 201);

    }

    @Test
    void testGetAPI() {

        //Check get API
        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .get();

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    //Update an existing book with valid data
    @Test
    public void testUpdatebookwithvaliddata() {


        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, title, author);

            Response response = given()
                    .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                    .contentType("application/json")
                    .body(requestBody)
                    .when()
                    .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());

            Assert.assertEquals( response.getStatusCode(), 200);

    }

    //Update book without authorization
    @Test
    void testUpdatebookwithoutauthorization() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, title, author);


        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());

        Assert.assertEquals(response.getStatusCode(), 401);
    }

    //Update a book with wrong authorization
    @Test
    void testUpdateabookwithwrongauthorization() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, title, author);


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, "Password")
                .contentType("application/json")
                .body(requestBody) // Set the request body
                .when()
                .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 401);

    }

    //Update a book with user authorization
    @Test
    void testUpdateabookwithuserauthorization() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, title, author);


        Response response = given()
                .auth().preemptive().basic(USER_USERNAME, USER_PASSWORD)
                .contentType("application/json")
                .body(requestBody) // Set the request body
                .when()
                .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 403);
        // Log the response for debugging purposes
    }

    //Update a book with invalid book ID
    @Test
    void testUpdateabookwithinvalidbookID() {

        String requestBody = "{\n" +
                "    \"id\": 5,\n" +
                "    \"title\": \"Family History\",\n" +
                "    \"author\": \"British historian Simon Sebag Montefiore\"\n" +
                "}";


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody) // Set the request body
                .when()
                .put("/5");

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(),404);

    }

    //Update a book with missing title
    @Test
    void testUpdateabookwithmissingtitle() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, author);


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody) // Set the request body
                .when()
                .put("/" + bookId);;

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 400);

    }

    //Update a book with missing author
    @Test
    void testUpdateabookwithmissingauthor() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "}", bookId, title);


        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody) // Set the request body
                .when()
                .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());
        Assert.assertEquals(response.getStatusCode(), 400);
    }

    //Update an integer value for the "title" parameter
    @Test
    public void testUpdateanIntegervalueforTitle() {


        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%d\",\n" +
                "    \"author\": \"%s\"\n" +
                "}", bookId, 123, author);

        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/" + bookId);

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());

        Assert.assertEquals(response.getStatusCode(), 400);

    }

    //Update an integer value for the "athour" parameter
    @Test
    public void testUpdateanIntegervalueforAthour() {


        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"%d\"\n" +
                "}", bookId, title, 123);

        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/" + bookId);;

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());

        Assert.assertEquals(response.getStatusCode(), 400);

    }

    //Update a "athour" only with same "id" and "title"
    @Test
    public void testUpdateaAthouronlywithsameIdandTitle() {

        String requestBody = String.format("{\n" +
                "    \"id\": %d,\n" +
                "    \"title\": \"%s\",\n" +
                "    \"author\": \"Jams Jackub\"\n" +
                "}", bookId, title);

        Response response = given()
                .auth().preemptive().basic(ADMIN_USERNAME, ADMIN_PASSWORD)
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/" + bookId);;

        System.out.println("Input: "+response.asString());
        System.out.println("Status Code: "+response.statusCode());

        Assert.assertEquals(response.getStatusCode(), 200);

    }

}
