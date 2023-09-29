import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.ReturnedUserDataJson;
import org.example.UserData;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
public class UserChangeDataTest {
    private String accessToken;
    Random random = new Random();
    String email = "test" + random.nextInt(1000) + "@yandex.ru";
    UserData newUser = new UserData(email, "password", "Username" );


    @Test
    @DisplayName("Check status code 200(success) for update user password with authorization")
    public void updateUserPasswordWithAuthorizationPositiveResult() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newUser.sendRequestUpdate(newUser.getEmail(), "newPassword", accessToken);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check status code 200(success) for update user email with authorization")
    public void updateUserEmailWithAuthorizationPositiveResult() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newUser.sendRequestUpdate("new-email@yandex.ru", newUser.getPassword(), accessToken);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Check status code 401(Unauthorized) for update user data without authorization")
    public void updateUserDataWithoutAuthorizationError() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newUser.sendRequestUpdate(newUser.getEmail(), "newPassword", "");
        response.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteUser(){
        Response responseDelete = newUser.sendRequestDelete(accessToken);
        responseDelete.then().assertThat()
                .body("message", equalTo("User successfully removed"));
    }
}
