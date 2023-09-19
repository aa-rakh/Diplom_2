import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.UserData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
@RunWith(Parameterized.class)
public class UserCreateParameterizedTest {
    private final String email;
    private final String password;
    private final String name;

    public UserCreateParameterizedTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    @Parameterized.Parameters
    public static Object[][] testDataGenerate() {
        return new Object[][]{
                {"", "1111", "Ivan"},
                {"testemail@yandex.ru", "", "Ivan"},
                {"testemail@yandex.ru", "1111", ""},
        };
    }
    @Test
    @DisplayName("Check status code 403(Email, password and name are required fields) for /auth/register")
    public void createUserWithoutRequiredDataError() {
        UserData newUser = new UserData(email, password, name);
        Response response = newUser.sendRequestCreate(newUser);
        response.then().assertThat()
                .statusCode(403)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
