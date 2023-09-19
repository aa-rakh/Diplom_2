import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.OrderData;
import org.example.ReturnedUserDataJson;
import org.example.UserData;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrdersTests {
    private String accessToken;
    Random random = new Random();
    String email = "test" + random.nextInt(1000) + "@yandex.ru";
    UserData newUser = new UserData(email, "password", "Username" );

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    public void GetOrdersAuthorizationTest() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        OrderData newOrder = new OrderData();
        Response response = newOrder.sendRequestGetOrder(accessToken);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованным пользователем")
    public void GetOrdersUnauthorizationTest() {
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        OrderData newOrder = new OrderData();
        Response response = newOrder.sendRequestGetOrder("");
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
