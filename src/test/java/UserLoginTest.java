import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.ReturnedUserDataJson;
import org.example.UserData;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;

public class UserLoginTest {
    private String accessToken;
    Random random = new Random();
    String email = "test" + random.nextInt(1000) + "@yandex.ru";
    UserData newUser = new UserData(email, "password", "Username" );
    @Test
    @DisplayName("Check status code 200(success) for /auth/login")
    public void loginUserPositiveResult() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newUser.sendRequestLogin(newUser.getEmail(), newUser.getPassword(), accessToken);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @After
    public void deleteUser(){
        Response responseDelete = newUser.sendRequestDelete(accessToken);
        responseDelete.then().assertThat()
                .body("message", equalTo("User successfully removed"));
    }
}
