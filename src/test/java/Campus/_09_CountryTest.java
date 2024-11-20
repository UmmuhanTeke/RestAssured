package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

//Campus Country Test planı
//
//Testler çalışmadan önce yapılacaklar
//Create Country den neler yapmak lazım
//
//faker
//BeforeClass da neler olacak ?
//{
//  login olup cookies i alıcam.
//
//  spece ekliyip, diğer requestlerin
//  kullanmasını sağlayacağım
//}
//
//Create Country{
//
//  spec
//}

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;


public class _09_CountryTest {

    RequestSpecification reqSpec;
    Faker faker = new Faker();
    String countryName = "";
    String code = "";
    String countryID = "";

    @BeforeClass
    public void setUp() {

        baseURI = "https://test.mersys.io";

        Map<String, String> userCredential = new HashMap<>();
        userCredential.put("username", "turkeyts");
        userCredential.put("password", "TechnoStudy123");
        userCredential.put("rememberMe", "true");

        Cookies cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(userCredential)

                        .when()
                        .post("/auth/login")

                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().detailedCookies();

        System.out.println("cookies = " + cookies);

        reqSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();
    }

    @Test
    public void createCountry() {
        //TODO: Burada null değerler de vardı istemediği için göndermedik.
        countryName = faker.address().country() + faker.number().digits(5);
        code = faker.address().countryCode() + faker.number().digits(5);

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", countryName);
        newUser.put("code", code);

        countryID =
                given()
                        .spec(reqSpec)
                        .body(newUser)

                        .when()
                        .post("/school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createCountry")
    public void createCountryNegative() {
        //aynı ülke kodu ve adı alınmalı.

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", countryName);
        newUser.put("code", code);

        given()
                .spec(reqSpec)
                .body(newUser)

                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("already"));
    }

    @Test(dependsOnMethods = "createCountryNegative")
    public void updateCountry() {
        countryName = "Yunanistan" + faker.number().digits(5);
        code = "123567"+faker.number().digits(5);

        Map<String, String> updCountry = new HashMap<>();
        updCountry.put("id", countryID);
        updCountry.put("name", countryName);
        updCountry.put("code", code);

        given()
                .spec(reqSpec)
                .body(updCountry)

                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(countryName))
                .body("code", equalTo(code));
    }

    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)//id gönd. diğer yolu pathParam("countryID", countryID)

                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsStringIgnoringCase("Country not found"));
    }
}