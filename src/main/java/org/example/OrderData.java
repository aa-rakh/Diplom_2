package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderData {

    @Step("Send POST request to /api/orders")
    public Response sendRequestCreateOrder(String ingredients, String accessToken){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        System.out.println("{\"ingredients\": \"" + ingredients + "\"}");
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body("{\"ingredients\": \"" + ingredients + "\"}")
                .when()
                .post("/api/orders");
        System.out.println(response.getBody().asString());
        return response;
    }

    @Step("Send GET request to /api/orders")
    public Response sendRequestGetOrder(String accessToken){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response orders = given()
                .header("Authorization", accessToken)
                .when()
                .get("/api/orders");
        System.out.println(orders.getBody().asString());
        return orders;
    }
}
