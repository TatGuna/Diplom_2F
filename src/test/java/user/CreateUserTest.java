package user;

import users.RestAssuredUser;
import users.UserClient;
import users.UsersCredentials;
import users.UsersData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest extends RestAssuredUser {

    private UsersData usersData;
    private UserClient userClient;
    private String token;
    boolean result;

    @Before
    public void setUp() {
        userClient = new UserClient();
        usersData = UsersData.getRandom();
    }

    @After
    public void tearDown() {
        userClient.deleteUser(token.substring(7));
    }

    @Test // Создать клиента и логин в системе
    @DisplayName("Create User")
    @Description("Создание клиента и логин в системе, извлечение токена")
    public void createUser() {
        ValidatableResponse response = userClient.createUser(usersData);
        token = response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
        result = response
                .extract()
                .path("success");
        Assert.assertTrue(result);
        UsersCredentials credentials = UsersCredentials.from(usersData);
        userClient.loginUser(credentials);
        response.assertThat()
                .statusCode(SC_OK);
    }

    @Test // Создание такого же клиента (проверить тело сообщения)
    @DisplayName("Create Same User")
    @Description("Повторное создание клиента с одинаковыми данными")
    public void createSameUser() {
        ValidatableResponse response = userClient.createUser(usersData);
        token = response
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("accessToken");
        boolean result = userClient.createUser(usersData)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .extract()
                .path("success");
        Assert.assertFalse(result);
    }
}
