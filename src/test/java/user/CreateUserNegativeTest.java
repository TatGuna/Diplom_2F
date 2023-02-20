package user;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import users.RestAssuredUser;
import users.UserClient;
import users.UsersData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserNegativeTest extends RestAssuredUser {
    private UsersData usersData;
    private UserClient userClient;
    private String token;


    @Before
    public void setUp() {
        userClient = new UserClient();
        usersData = UsersData.getRandom();
    }

    @After
    public void tearDown() {
        if (token != null) {
            userClient.deleteUser(token.substring(7));
        }
        ;
    }

    @Test
    @DisplayName("Create User Without Password")
    @Description("Создание клиента без пароля")
    public void createUserWithoutPassword() {
        usersData.setPassword("");
        userClient.createUser(usersData)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(BODY_403));
    }

    @Test
    @DisplayName("Create User Without Email")
    @Description("Создание клиента без email")
    public void createUserWithoutEmail() {
        usersData.setEmail("");
        userClient.createUser(usersData)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(BODY_403));
    }

    @Test
    @DisplayName("Create User Without Name")
    @Description("Создание клиента без имени")
    public void createUserWithoutName() {
        usersData.setName("");
        userClient.createUser(usersData)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(BODY_403));
    }

    @Test
    @DisplayName("Create User With Empty Body")
    @Description("Создание клиента с пустым телом запроса")
    public void createUserWithEmptyBody() {
        usersData.setName(null);
        usersData.setEmail(null);
        usersData.setPassword(null);

        ValidatableResponse response = userClient.createUser(usersData);
        token = response
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(BODY_403))
                .extract()
                .path("accessToken");
        boolean result = userClient.createUser(usersData)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body(equalTo(BODY_403))
                .extract()
                .path("success");
        Assert.assertFalse(result);

    }
}
