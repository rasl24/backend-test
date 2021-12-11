package com.geekbrains.backend.test.imgur;

import com.geekbrains.backend.test.FunctionalTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ImgurApiFunctionalTest extends FunctionalTest {

//    private static Properties properties;
//    private static String TOKEN;
//
//    @BeforeAll
//     static void beforeAll() throws Exception {
//        // чтение свойств
//        properties = readProperties();
//        // запись в baseURI это свойство
//        RestAssured.baseURI = properties.getProperty("imgur-api-url");
//        TOKEN = properties.getProperty("imgur-api-token");
//    }

    static Map<String, String> headers = new HashMap<>();

    @BeforeAll
    static void setUp() {
        headers.put("Authorization", "Bearer c2b43824a9f88b6ca7672ffe83e649713c65451e");
    }

    @Test
    void getAccountBase(){
        String url = "https://api.imgur.com/3/";
        String userName = "rasilsultanov";
        given()
                .headers(headers)
//                .auth()
//                .oauth2(TOKEN)
                .log()
                .all()
                .expect()
                .body("data.id", is(157784245)) // проверка строки ответа
                .log()
                .all()
                .when()
                //.get("account/" + userName);
                .get(url + "account/" + userName);
    }

    @Test
    void postImageTest(){
        String url = "https://api.imgur.com/3/";
        given()
                .headers(headers)
                .multiPart("image", getFileResource("Lighthouse.jpg"))
                .formParam("name", "Picture")
                .formParam("title", "The best picture!!!")
                .log()
                .all()
                .expect()
                .body("data.size", is(91480))
                .body("data.type", is("image/jpeg"))
                .body("data.name", is("Picture"))
                .body("data.title", is("The best picture!!!"))
                .log()
                .all()
                .when()
                .post(url + "upload");
    }

    // Домашнее задание

    @Test
    void postUpdateInfoImageTest(){
        String url = "https://api.imgur.com/3/";
        String imageHash = "YI93OVi";
        given()
                .headers(headers)
                .formParam("description", "Picture")
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "image/" + imageHash)
                .then()
                .statusCode(200);
    }

    @Test
    void postFavoriteAnImageTest(){
        String url = "https://api.imgur.com/3/";
        String imageHash = "YI93OVi";
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .body("data", is("favorited"))
                .log()
                .all()
                .when()
                .post(url + "image/" + imageHash + "/favorite")
                .then()
                .statusCode(200);
    }

    @Test
    void postDocTest(){
        String url = "https://api.imgur.com/3/";
        given()
                .headers(headers)
                .multiPart("image", getFileResource("Hello.txt"))
                .log()
                .all()
                .expect()
                .body("data.error", is("We don't support that file type!"))
                .log()
                .all()
                .when()
                .post(url + "upload")
                .then()
                .statusCode(400);
    }

    @Test
    void postUpdateInfoImageUnAuthedTest(){
        String url = "https://api.imgur.com/3/";
        String imageHash = "YI93OVi";
        given()
                .formParam("description", "Picture")
                .log()
                .all()
                .expect()
                .body("data.error", is("Authentication required"))
                .log()
                .all()
                .when()
                .post(url + "image/" + imageHash)
                .then()
                .statusCode(401);
    }

    @Test
    void deleteImageTest(){
        String url = "https://api.imgur.com/3/";
        String imageHash = "YI93OVi";
        given()
                .headers(headers)
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .delete(url + "image/" + imageHash)
                .then()
                .statusCode(200);
    }
}
