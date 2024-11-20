package GoRest;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class _07_GoRestUsersTest {
//TODO: INTERVİEW DA GELMİŞ !!!

    Faker faker = new Faker();
    RequestSpecification reqSpec;
    int userID; //id kullanacağız buraya tanımladık.

    @BeforeClass
    public void setUp() {

        baseURI = "https://gorest.co.in/public/v2/users";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer f92bf3f56439b631d9ed928b3540968e747c8e75309c876420fb349cbb420ed1") //token
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createUser() {
        //TODO: body mi hazırlayacağım

        //{
        //"name":"{{$randomFullName}}",
        //"gender":"Male",
        //"email":"{{$randomEmail}}",
        //"status":"active"
        //} //51. satıra direkt bunu eklememek için MAP tercih ettik.(BODY KISMI)

        String rndFullName = faker.name().fullName();
        String rndEmail = faker.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", rndFullName);
        newUser.put("gender", "Male");
        newUser.put("email", rndEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("") //http ile başlamıyorsa baseURI, 19. satır gelicek

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");

        System.out.println("User ID: " + userID);
    }

    @Test(dependsOnMethods = "createUser")
    public void getUserByID() {

        given()
                .spec(reqSpec)
                .log().uri()

                .when()
                .get("" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID));
    }

    @Test(dependsOnMethods = "getUserByID") //bu aşamadan sonra class çalıştırılmalı.
    public void updateUser()
    {
        String updName="İsmet Temur";

        Map<String,String> updUser=new HashMap<>();
        updUser.put("name",updName);

        given()
                .spec(reqSpec)
                .body(updUser)

                .when()
                .put(""+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo(updName))
        ;
    }

    @Test(dependsOnMethods = "updateUser")
    public void deleteUser(){
        given()
                .spec(reqSpec)

                .when()
                .delete(""+userID)

                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteUser")
    public void deleteUserNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete(" "+userID)

                .then()
                .statusCode(404);
    }
}