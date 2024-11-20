import Model.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class _05_Tasks {
    /**
     * Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void extractJsonAll_POJO() {

                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("title", equalTo("quis ut nam facilis et officia qui"));
    }

    /**
     * Task 2
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     *a) expect response completed status to be false(hamcrest)
     *b) extract completed field and testNG assertion(testNG)
     */

    @Test
    public void extractJsonAll_POJO2() {

        //a)
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false)); //hamcrest ile assertion

        //b)
        boolean completedData=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed");

        Assert.assertFalse(completedData);
    }

    /** Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * Converting Into POJO body data and write
     */
    @Test
    public void extractJsonAll_POJO3() {

        ToDo todoObject =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .extract().body().as(ToDo.class);

        System.out.println("toDo = " + todoObject);
        Assert.assertEquals(todoObject.getId(), 2);
    }
}