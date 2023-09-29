import io.restassured.response.Response;
import org.example.UserData;
import org.example.ReturnedUserDataJson;
import org.junit.After;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserCreateTest {

    private String accessToken;
    Random random = new Random();
    String email = "test" + random.nextInt(1000) + "@yandex.ru";
    UserData newUser = new UserData(email, "password", "Username" );
    @Test
    @DisplayName("Check status code 200(success) for /auth/register")
    public void createUserPositiveResult() {
        Response response = newUser.sendRequestCreate(newUser);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        accessToken = response.getBody().as(ReturnedUserDataJson.class).getAccessToken();
    }

    @Test
    @DisplayName("Check status code 403(User already exists) for /auth/register")
    public void createUserTwiceGetError() {
        accessToken = newUser.sendRequestCreate(newUser).getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response secondResponse = newUser.sendRequestCreate(newUser);
        secondResponse.then().assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("User already exists"));
    }

    @After
    public void deleteUser(){
        Response responseDelete = newUser.sendRequestDelete(accessToken);
        responseDelete.then().assertThat()
                .body("message", equalTo("User successfully removed"));
    }

}
