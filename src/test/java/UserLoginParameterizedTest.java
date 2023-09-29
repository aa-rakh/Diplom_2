import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.ReturnedUserDataJson;
import org.example.UserData;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserLoginParameterizedTest {
    private final String email;
    private final String password;
    private String accessToken;

    public UserLoginParameterizedTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Object[][] testDataGenerate() {
        return new Object[][]{
                {"testemailAAA@yandex.ru", ""},
                {"", "password"},
                {"", ""},
                {"testemailAAA@yandex.ru", "passwor111d"},
                {"incorrect_emailAAA@yandex.ru", "password"},
        };
    }

    @Test
    @DisplayName("Check status code 401(email or password are incorrect) for /auth/login")
    public void loginUserWithoutRequiredDataError() {
        UserData newUser = new UserData("testemailAAA@yandex.ru", "password", "Username");
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newUser.sendRequestLogin(email, password, accessToken);
        response.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void deleteUser(){
        UserData newUser = new UserData("testemailAAA@yandex.ru", "password", "Username");
        Response responseDelete = newUser.sendRequestDelete(accessToken);
        responseDelete.then().assertThat()
                .body("message", equalTo("User successfully removed"));
    }

}
