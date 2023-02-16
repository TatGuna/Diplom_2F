package order;

import org.junit.After;
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

public class GetUserOrdersTest extends RestAssuredUser {

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

    @Test
    @DisplayName("Get Orders")
    @Description("Получение списка заказов клиента")
    public void getOrders() {
        setUp();
        boolean status = reqSpec
                .auth().oauth2(token.substring(7))
                .get(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("success");
        Assert.assertTrue(status);
        userClient.deleteUser(token.substring(7));
    }

    @Test
    @DisplayName("Get Orders Without Authorisation")
    @Description("Получение списка заказов клиента без авторизации")
    public void getOrdersWithoutAuth() {
        reqSpec
                .get(ORDERS)
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo(UNAUTH_LOGIN));
    }

    @After
    public void delete() {
        userClient.deleteUser(token); //то был не Гарольд, скрывающий боль,
        //а Гарольд, скрывающий косяк в тестах. Но в итоге он даже косяк в тестах скрыть не мог,
        //что уж там о боли говорить))
    }
}
