package com.geekbrains.backend.test.imgur;

import com.geekbrains.backend.test.FunctionalTest;
import io.restassured.RestAssured;
import io.restassured.authentication.OAuth2Scheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.internal.AuthenticationSpecificationImpl;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

// это для того, чтобы указывать у тестов, каким по очереди его запускать
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ImgurApiFunctionalTest extends FunctionalTest {

    private static String CURRENT_IMAGE_ID;
    private static AuthenticationSpecification auth;
    private static String TOKEN = "c2b43824a9f88b6ca7672ffe83e649713c65451e";
    private static RequestSpecification requestSpecification;
    private static String url = "https://api.imgur.com/3/";
    private static ResponseSpecification responseSpecification;
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
        requestSpecification = new RequestSpecBuilder()
                .setAuth(new OAuth2Scheme())
                .build();
        auth = new AuthenticationSpecificationImpl(requestSpecification);
        requestSpecification = auth.oauth2(TOKEN);
    }

    @Test
    @Order(1)
    void getAccountBase(){
        String userName = "rasilsultanov";
//        RequestSpecification req = new RequestSpecBuilder()
//                .build();
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("data.id", is(157784245))
                .build();
        given()
                .spec(requestSpecification)
//                .auth()
//                .oauth2(TOKEN)
               // .spec(req)
                .log()
                .all()
                .expect()
                .spec(responseSpecification)
                .log()
                .all()
                .when()
                //.get("account/" + userName);
                .get(url + "account/" + userName);
    }

    @Test
    @Order(2)
    void postImageTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("data.id", is(157784245))
                .expectBody("data.size", is(91480))
                .expectBody("data.type", is("image/jpeg"))
                .expectBody("data.name", is("Picture"))
                .expectBody("data.title", is("The best picture!!!"))
                .build();
        // присвоение значения imageId переменной CURRENT_IMAGE_ID
        CURRENT_IMAGE_ID = given()
                .spec(requestSpecification)
                .multiPart("image", getFileResource("Lighthouse.jpg"))
                .formParam("name", "Picture")
                .formParam("title", "The best picture!!!")
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "upload")
            // достаем значение imageId из ответа
                .body()
                .jsonPath()
                .getString("data.id");
    }

//    @Test
//    @Order(3)
//    void deleteImageById(){
//        given()
//                .spec(requestSpecification)
//                .log()
//                .all()
//                .expect()
//                .body("status", is(200))
//                .log()
//                .all()
//                .when()
//                .delete(url + "image/" + CURRENT_IMAGE_ID);
//    }

    // Задание: Применить ResponseSpecification к тестам
    // Домашнее задание

    @Test
    @Order(4)
    void postUpdateInfoImageTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        given()
                .spec(requestSpecification)
                .formParam("description", "Picture")
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "image/" + CURRENT_IMAGE_ID);
    }

    @Test
    @Order(5)
    void postFavoriteAnImageTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("data", is("favorited"))
                .expectStatusCode(200)
                .build();
        given()
                .spec(requestSpecification)
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "image/" + CURRENT_IMAGE_ID + "/favorite");
    }

    @Test
    @Order(6)
    void postDocTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("data.error", is("We don't support that file type!"))
                .expectStatusCode(400)
                .build();
        given()
                .spec(requestSpecification)
                .multiPart("image", getFileResource("Hello.txt"))
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "upload");
    }

    @Test
    @Order(7)
    void postUpdateInfoImageUnAuthedTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectBody("data.error", is("Authentication required"))
                .expectStatusCode(401)
                .build();
        given()
                .formParam("description", "Picture")
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .post(url + "image/" + CURRENT_IMAGE_ID);
    }

    @Test
    @Order(8)
    void deleteImageTest(){
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        given()
                .spec(requestSpecification)
                .log()
                .all()
                .expect()
                .log()
                .all()
                .when()
                .delete(url + "image/" + CURRENT_IMAGE_ID);
    }

//    @Test
//    @Order(9)
//    void testCreateComment(){
//        // комментировать можно только публичные картинки
//        given()
//                .spec(requestSpecification)
//                .formParam("image_id", "X0h0bvT")
//                .formParam("comment", "Hello world")
//                .log()
//                .all()
//                .expect()
//                .body("success", is(true))
//                .body("status", is(200))
//                .log()
//                .all()
//                .when()
//                .post(url + "comment");
//    }
}
