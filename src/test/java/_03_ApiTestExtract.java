import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class _03_ApiTestExtract {

    @Test
    public void extractingJsonPath(){
        String countryName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country"); //PATH i country olan değeri EXTRACT yap
        System.out.println("Country Name: "+ countryName);
        Assert.assertEquals(countryName, "United States");
    }

    @Test
    public void extractingJsonPath2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint den dönen
        // places dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız

        String stateName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state"); //PATH i country olan değeri EXTRACT yap

        System.out.println("State Name: "+ stateName);
        Assert.assertEquals(stateName, "California");
    }

    @Test
    public void extractingJsonPath3() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.

        int limit =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("meta.pagination.limit"); //PATH i country olan değeri EXTRACT yap

        System.out.println("Limit: "+ limit);
        Assert.assertTrue(limit==10);
    }

    @Test
    public void extractingJsonPath4() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // data daki bütün id leri nasıl alırız.

        ArrayList<Integer> idler=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id"); //PATH i country olan değeri EXTRACT yap

        System.out.println("idler: "+ idler);
    }

    @Test
    public void extractingJsonPath5() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız.

        ArrayList<String> names=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name"); //PATH i country olan değeri EXTRACT yap

        System.out.println("names :"+ names);
    }


    @Test
    public void extractingJsonPathResponsAll() {
        // TODO: değerler geldi response ile.

        Response donenData=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response();

        List<Integer> idler=donenData.path("data.id");
        List<String> names=donenData.path("data.name");
        int limit=donenData.path("meta.pagination.limit");

        System.out.println("idler :"+ idler);
        System.out.println("names :"+ names);
        System.out.println("limit :"+ limit);

        Assert.assertTrue(idler.contains(7522200));
        Assert.assertTrue(names.contains("Varalakshmi Mehrotra"));
        Assert.assertTrue(limit == 10);
    }
}