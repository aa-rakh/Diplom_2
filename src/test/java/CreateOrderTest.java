import io.qameta.allure.junit4.DisplayName;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.example.Ingredients;
import org.example.OrderData;
import org.example.ReturnedUserDataJson;
import org.example.UserData;
import org.junit.After;
import org.junit.Test;

import java.util.Random;

import static io.restassured.path.xml.XmlPath.CompatibilityMode.HTML;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {
    private String accessToken;
    Random random = new Random();
    String email = "test" + random.nextInt(1000) + "@yandex.ru";
    UserData newUser = new UserData(email, "password", "Username" );

    @Test
    @DisplayName("Создание заказа с ингредиентами с авторизацией")
    public void CreateOrderWithIngredientsAuthorizationTest() {
        Ingredients ingredient = new Ingredients();
        String ingredients = ingredient.sendRequestGetIngredients();
        OrderData newOrder = new OrderData();
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newOrder.sendRequestCreateOrder(ingredients, accessToken);
        response.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    public void CreateOrderWithIngredientsWithoutAuthorizationTest() {
        // по документации неавторизованный пользователь не может создавать заказы
        // однако, в реализации заказы успешно создаются, поэтому тест падает
        Ingredients ingredient = new Ingredients();
        String ingredients = ingredient.sendRequestGetIngredients();
        OrderData newOrder = new OrderData();
        newUser.sendRequestCreate(newUser);
        Response response = newOrder.sendRequestCreateOrder(ingredients, "");
        response.then().assertThat()
                .statusCode(401);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов с авторизацией")
    public void CreateOrderWithoutIngredientsAuthorizationTest() {
        OrderData newOrder = new OrderData();
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Response response = newOrder.sendRequestCreateOrder("", accessToken);
        response.then().assertThat()
                .statusCode(400)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов с авторизацией")
    public void CreateOrderIncorrectIngredientsHashAuthorizationTest() {
        OrderData newOrder = new OrderData();
        Response responseCreate = newUser.sendRequestCreate(newUser);
        accessToken = responseCreate.getBody().as(ReturnedUserDataJson.class).getAccessToken();
        Ingredients ingredient = new Ingredients();
        String ingredients = ingredient.sendRequestGetIngredients() + "incorrect";
        System.out.println(ingredients);
        Response response = newOrder.sendRequestCreateOrder(ingredients, accessToken);
        response.then().assertThat()
                .statusCode(500);
        XmlPath path = new XmlPath(HTML, response.then().contentType(ContentType.HTML).extract().response().getBody().asString());
        System.out.println(path.getString("html.head.title"));
        assertEquals(path.getString("html.head.title"), "Error");
        assertEquals(path.getString("html.body.pre"), "Internal Server Error");
    }

    @After
    public void deleteUser(){
        Response responseDelete = newUser.sendRequestDelete(accessToken);
        responseDelete.then().assertThat()
                .body("message", equalTo("User successfully removed"));
    }

}
