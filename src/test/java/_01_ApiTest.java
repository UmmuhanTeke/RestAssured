import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class _01_ApiTest {

    @Test
    public void Test1() {

        // 1- Endpoint i çağırmadan önce hazırlıkların yapıldığı bölüm : Request, gidecek body, token
        // 2- Endpoint in çağırıldığı bölüm : Endpoint in çağrılması(METOD: GET,POST..)
        // 3- Endpoint çağrıldıktan sonraki bölüm : Response, Tes(Assert), data

        given().
                //1. bölümle ilgili işler: giden body, token
                        when().
                //2. bölümle ilgili işler: metod, endpoint

                        then()
        //3. bölümle ilgili işler; gelen data, assert, test
        ;
    }

    @Test
    public void statusCodeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() //dönen data kısmı
                //.log().all() //dönen bütün bilgileri
                .statusCode(200); // dönen değer 200 e eşit mi, ASSERT
    }

    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() //dönen data kısmı
                .statusCode(200) // dönen değer 200 e eşit mi, ASSERT
                .contentType(ContentType.JSON); //dönen data nın TİPİ
    }

    @Test
    public void checkCountryInResponseBody() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() //dönen data kısmı
                .statusCode(200) // dönen değer 200 e eşit mi, ASSERT
                .contentType(ContentType.JSON) //dönen data nın TİPİ
                .body("country", equalTo("United States")); //country yi dışarı almadan
        //bulunduğu yeri(path) vererek içeride assert yapıyorum. Bunu hamcrest kütüphanesi yapıyor.

        // pm.test("Ulke Bulunamadı", function () {
        // pm.expect(pm.response.json().message).to.eql("Country not found");
        //    });
    }

    @Test
    public void checkCountryInResponseBody2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // places dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"));
    }

    @Test
    public void checkHasItem() {
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız

        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"));
        //tırnak koymalısın 'place name' arasında
        // boşluk old. için string ifadeyi böyle algılayacak
        // Rest Assured, JSON yanıtını otomatik olarak analiz edip her bir "place" öğesindeki "place name" değerlerini
        // aldı ve bu öğeleri bir koleksiyon olarak kontrol etti. Bu sayede listeyi manuel olarak oluşturmamıza gerek kalmadı.
    }

    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places", hasSize(1));
    }

    @Test
    public void combiningTest() {
        //TODO: Birden fazla testin kontrolünü de yapabilirim.

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"));
    }

    @Test
    public void pathParamTest() {
        //TODO: URL üzerinde parametre ekleyebilirim.

        given()
                .pathParam("ulke", "us")
                .pathParam("postaKodu", 90210)
                .log().uri() //oluşacak endpoint i yazdıralım

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}")

                .then()
                .log().body();
    }

    @Test
    public void queryParamTest() {

        given()
                .param("page", 3)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users") //https://gorest.co.in/public/v1/users?page=3(eski hali)

                .then()
                .log().body();
    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10 ; i++) {
            given()
                    .param("page", i)

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(i));
        }
    }
}