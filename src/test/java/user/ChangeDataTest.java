package user;

import json.LoginResponse;
import users.RestAssuredUser;
import users.UserClient;
import users.UsersData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;


public class ChangeDataTest extends RestAssuredUser {
    UsersData usersData;
    private UserClient userClient;
    private String token;
    String EXIST_EMAIL = "{\"email\":\"tat-guna@yandex.ru\"}";

    @Before
    public void setUp() {
        userClient = new UserClient();
        usersData = UsersData.getRandom();
        ValidatableResponse response = userClient.createUser(usersData);
        token = response
                .extract()
                .path("accessToken");
    }

    @After
    public void tearDown() {
        userClient.deleteUser(token.substring(7));
    }

    @Test // Изменение имени с токеном
    @DisplayName("Change User Name")
    @Description("Смена имени клиента")
    public void changeUserName() {
        LoginResponse response = reqSpec
                .auth().oauth2(token.substring(7))
                .body(newUserName)
                .patch(UPDATE_DATA)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .body().as(LoginResponse.class);
        Assert.assertEquals(response.getUser().getName(), randomName);
    }

    @Test // Изменение имени без авторизации
    @DisplayName("Change User Name Without Auth")
    @Description("Смена имени клиента без авторизации")
    public void changeUserNameWithoutAuth() {
        reqSpec
                .body(newUserName)
                .patch(UPDATE_DATA)
                .then().log().all()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo(UNAUTH_LOGIN));
    }

    @Test
    @DisplayName("Change User Email")
    @Description("Смена email клиента")
    public void changeUserEmail() {
        LoginResponse response = reqSpec
                .auth().oauth2(token.substring(7))
                .body(anotherEmail)
                .patch(UPDATE_DATA)
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .body().as(LoginResponse.class);
        Assert.assertEquals(response.getUser().getEmail(), randomEmail);

    }

    @Test
    @DisplayName("Change User Email On Exist")
    @Description("Смена email клиента на уже существующий")
    public void changeUserEmailOnExist() {
        reqSpec
                .auth().oauth2(token.substring(7))
                .body(EXIST_EMAIL)
                .patch(UPDATE_DATA)
                .then().log().all()
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(ERROR_EXIST_EMAIL));
    }
}
