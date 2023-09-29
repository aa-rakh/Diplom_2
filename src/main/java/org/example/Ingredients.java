package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class Ingredients {

    @Step("Send GET request to /api/ingredients")
    public String sendRequestGetIngredients(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        String ingredients = given()
                .get("/api/ingredients")
                .then().extract().path("data[1]._id").toString();
        return ingredients;
    }
}
