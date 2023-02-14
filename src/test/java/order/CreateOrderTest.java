package order;

import users.RestAssuredUser;
import users.UserClient;
import users.UsersData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest extends RestAssuredUser {

    UsersData usersData;
    private UserClient userClient;
    private String token;

    public void setUp() {
        userClient = new UserClient();
        usersData = UsersData.getRandom();
        ValidatableResponse response = userClient.createUser(usersData);
        token = response
                .extract()
                .path("accessToken");
    }

    @Test // Обычный заказ с авторизацией и ингредиентом
    @DisplayName("Create Order")
    @Description("Создание заказа с ингредиентом")
    public void createOrder() {
        setUp();
        boolean status = reqSpec
                .auth().oauth2(token.substring(7))
                .body(ingredients)
                .post(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
        Assert.assertTrue(status);
        userClient.deleteUser(token.substring(7));
    }

    @Test // Обычный заказ без авторизации с ингредиентом
    @DisplayName("Create Order Withot Auth")
    @Description("Создание заказа с ингредиентом без авторизации")
    public void createOrderWithoutAuth() {
        boolean status = reqSpec
                .body(ingredients)
                .post(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
        Assert.assertTrue(status);
    }

    @Test // Заказ без ингредиента
    @DisplayName("Create Order Without Ingredients")
    @Description("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        reqSpec
                .body(empty_ingredients)
                .post(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body(equalTo(ORDER_EMPTY_BODY));
    }

    @Test // Заказ с невалидным ид ингредиента
    @DisplayName("Create Order With Wrong Ingredient ID")
    @Description("Создание заказа с  неправильным id ингредиента")
    public void createOrderWithWrongIngredient() {
        reqSpec
                .body(wrong_ingredients)
                .post(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
