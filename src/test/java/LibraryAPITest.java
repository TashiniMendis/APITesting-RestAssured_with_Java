import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.fail;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class LibraryAPITest {

    private static Properties properties;

    @BeforeClass
    public void setUp() {
        // Load configuration from properties file
        properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        RestAssured.baseURI = properties.getProperty("baseURI");
        RestAssured.authentication = RestAssured.preemptive().basic(
                properties.getProperty("adminUsername"),
                properties.getProperty("adminPassword")
        );
    }
    //1
    @Test
    public void testUnauthorizedCreateBook() {
        String requestBody = """
        {
            "id": 130,
            "title": "Unauthorized Book",
            "author": "Author Name -1"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();
        assertStatusCodeAndPrintResponse(401, "Expected status code 401 (Unauthorized)", response);
    }
    //2
    @Test
    void createNewBook() {
        String requestBody = """
        {
            "id": 6,
            "title": "Jadunama",
            "author": "Javed Akhtar and Arvind Mandloi"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == 208) {
            assertStatusCodeAndPrintResponse(208, "Expected status code 208", response);
        } else if (statusCode == 201) {
            assertStatusCodeAndPrintResponse(201, "Expected status code 201", response);
        } else {
            fail("Failed to create a book : " + statusCode);
        }
    }
    //3
    @Test
    void createNewBookWithEmptyTitle() {
        String requestBody = """
        {
            "id": 7,
            "title": "",
            "author": "BBB Name"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        if (statusCode == 400) {
            assertStatusCodeAndPrintResponse(400, "Expected status code 400", response);
        }else {
            fail("Failed to create a book with empty title: " + statusCode);
        }
    }
    //4
    @Test
    void createDuplicateBook() {
        String requestBody = """
        {
            "id": 6,
            "title": "Jadunama",
            "author": "Javed Akhtar and Arvind Mandloi"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == 208) {
            assertStatusCodeAndPrintResponse(208, "Expected status code 208", response);
        } else {
            fail("Failed to create a duplicate book: " + statusCode);
        }
    }

    //5
    @Test
    void createBookWithMissingAuthor() {
        String requestBody = """
        {
            "id": 8000,
            "title": "Book Title3",
            "author": ""
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        System.out.println(statusCode);

        if (statusCode == 400) {
            assertStatusCodeAndPrintResponse(400, "Expected status code 400", response);
        } else {
            fail("Failed to create a book with missing author: " + statusCode);
        }
    }

    //6
    @Test
    void createBookWithMissingId() {
        String requestBody = """
        {
            "id": ,
            "title": "Valid Book",
            "author": "Author Name"
        }
        """;

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/books");

        int statusCode = response.getStatusCode();

        if (statusCode == 201) {
            assertStatusCodeAndPrintResponse(201, "Expected status code 201", response);
        } else {
            fail("Failed to create a book with valid data: " + statusCode);
        }
    }




    @Test
    public void updateBook() {
        String requestBody = """
    {
        "id": 1,
        "title": "Jadma",
        "author": "Javed Akhtar and Arvind Mandloi"
    }
    """;

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/books/10");

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        System.out.println("Response : " + responseBody); // Print the response body

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 208) {
            Assert.assertEquals(statusCode, 208, "Expected status code 208");
        } else if (statusCode == 400) {
            Assert.assertEquals(statusCode, 400, "Expected status code 400");
            Assert.assertTrue(responseBody.toLowerCase().contains("Book id is not matched".toLowerCase()), "Error message not found in the response");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to update book with status code: " + statusCode);
        }
    }



    @Test
    public void deleteBook() {
        Response response = RestAssured.given()
                .when()
                .delete("/api/books/1");

        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to delete book with status code: " + statusCode);
        }

        System.out.println("Response : " + responseBody);
    }

    @Test
    public void getAllBooks() {
        Response response = RestAssured.get("/api/books");
        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        Assert.assertEquals(statusCode, 200, "Expected status code 200");
        System.out.println("Response : " + responseBody);
    }

    @Test
    public void getBookByID() {
        Response response = RestAssured.get("/api/books/5");
        int statusCode = response.getStatusCode();
        String responseBody = response.asString();

        if (statusCode == 200) {
            Assert.assertEquals(statusCode, 200, "Expected status code 200");
        } else if (statusCode == 404) {
            Assert.assertEquals(statusCode, 404, "Expected status code 404");
        } else {
            Assert.fail("Failed to get book by ID with status code: " + statusCode);
        }

        System.out.println("Response : " + responseBody);
    }

    private void assertStatusCodeAndPrintResponse(int expectedStatusCode, String assertionMessage, Response response) {
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, assertionMessage);
        System.out.println("Response : " + response.asString());
    }
}
