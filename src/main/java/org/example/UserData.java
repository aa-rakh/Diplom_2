package org.example;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class UserData {
    private String email;
    private String password;
    private String name;

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserData() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Step("Send POST request to /api/auth/register")
    public Response sendRequestCreate(UserData newUser){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(newUser)
                .when()
                .post("/api/auth/register");
        System.out.println(response.getBody().asString());
        return response;
    }

    @Step("Send POST request to /api/auth/login")
    public Response sendRequestLogin(String email, String password, String accessToken) {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        System.out.println("{\"email\": \"" + email + "\", \"password\": \""  + password + "\"}");
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body("{\"email\": \"" + email + "\", \"password\": \""  + password + "\"}")
                .when()
                .post("/api/auth/login");
        System.out.println(response.getBody().asString());
        return response;
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response sendRequestUpdate(String email, String password, String accessToken) {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body("{\"email\": \"" + email + "\", \"password\": \""  + password + "\"}")
                .patch("/api/auth/user");
        System.out.println(response.getBody().asString());
        return response;
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response sendRequestDelete(String accessToken) {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Response response = given()
                .header("Authorization", accessToken)
                .delete("/api/auth/user");
        System.out.println(response.getBody().asString());
        return response;
    }
}

