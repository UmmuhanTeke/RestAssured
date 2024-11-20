package Campus;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;

public class _10_CitizenshipTest extends CampusParent {
    String citizenshipsName = "";
    String citizenshipsID = "";

    @Test
    public void createCitizenships() {
        //TODO: Burada null değerler de vardı istemediği için göndermedik.
        citizenshipsName = faker.nation().nationality() + faker.number().digits(5);

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", citizenshipsName);
        newUser.put("shortName", "12345");

        citizenshipsID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("/school-service/api/citizenships")

                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id");

        System.out.println(citizenshipsID);
    }

    @Test(dependsOnMethods = "createCitizenships")
    public void createCitizenshipsNegative() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", citizenshipsName);
        newUser.put("shortName", "12345");

        given()
                .spec(reqSpec)
                .body(newUser)

                .when()
                .post("/school-service/api/citizenships")

                .then()
                .statusCode(400)
                .log().body()
                .body("message", containsStringIgnoringCase("already exists"));
    }

    @Test(dependsOnMethods = "createCitizenshipsNegative")
    public void updateCitizenships() {
        citizenshipsName = "Yunan" + faker.number().digits(5);

        Map<String, String> updCitizenships = new HashMap<>();
        updCitizenships.put("id", citizenshipsID);
        updCitizenships.put("name", citizenshipsName);
        updCitizenships.put("shortName", "12345");

        given()
                .spec(reqSpec)
                .body(updCitizenships)

                .when()
                .put("/school-service/api/citizenships")

                .then()
                .statusCode(200)
                .log().body()
                .body("name", equalTo(citizenshipsName));
    }

    @Test(dependsOnMethods = "updateCitizenships")
    public void deleteCitizenships() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/citizenships/" + citizenshipsID)

                .then()
                .statusCode(200)
                .log().body();
    }

    @Test(dependsOnMethods = "deleteCitizenships")
    public void deleteCitizenshipsNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/citizenships/" + citizenshipsID)

                .then()
                .statusCode(400)
                .log().body()
                .body("message", containsStringIgnoringCase("country not found"));
    }
}