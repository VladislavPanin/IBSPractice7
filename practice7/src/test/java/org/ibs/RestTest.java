package org.ibs;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Практика 7:
 * Часть задания 1: Попробуйте создать товар и получить список товаров с помощью API
**/
public class RestTest {

    private String myCookie;

    @Test
    void launch(){
        getCookies();
        addFruit();
        getProducts();
    }

    void addFruit() {
        RestAssured.baseURI = "http://localhost:8080";

        Response response = given()
                .header("Accept", "*/*")
                .header("Content-Type", "application/json")
                .cookie("JSESSIONID", myCookie)
                .body("{\"name\":\"Ананас\",\"type\":\"FRUIT\",\"exotic\":true}")
                .when()
                .post("/api/food");

        response.prettyPrint();
        response.then()
                .statusCode(200);
    }

    void getCookies() {
        Response response = given()
                .header("Accept", "*/*")
                .when()
                .get("http://localhost:8080/api/food");

        myCookie = response.getCookie("JSESSIONID");
        response.prettyPrint();
        response.then().statusCode(200);

        System.out.println(myCookie);
    }

    void getProducts(){
        given()
                .baseUri("http://localhost:8080")
                .cookie("JSESSIONID", myCookie)
                .when()
                .get("/api/food")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body(
                        "[0].name", equalTo("Апельсин"),
                        "[0].type", equalTo("FRUIT"),
                        "[0].exotic", equalTo(true),
                        "[1].name", equalTo("Капуста"),
                        "[1].type", equalTo("VEGETABLE"),
                        "[1].exotic", equalTo(false),
                        "[2].name", equalTo("Помидор"),
                        "[2].type", equalTo("VEGETABLE"),
                        "[2].exotic", equalTo(false),
                        "[3].name", equalTo("Яблоко"),
                        "[3].type", equalTo("FRUIT"),
                        "[3].exotic", equalTo(false),
                        "[4].name", equalTo("Ананас"),
                        "[4].type", equalTo("FRUIT"),
                        "[4].exotic", equalTo(true));
    }
}
