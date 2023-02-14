package user;

import users.RestAssuredUser;
import users.UserClient;
import users.UsersCredentials;
import users.UsersData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserNegativeTest extends RestAssuredUser {
    private UsersData usersData;
    private UserClient userClient;
    private String token;

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

    @Test
    @DisplayName("Login User With Incorrect Password")
    @Description("Логин клиента в системе с неправильным паролем")
    public void loginUserWithIncorrectPassword() {
        usersData.setPassword(usersData.getPassword()+1);
        UsersCredentials credentials = UsersCredentials.from(usersData);
        userClient.loginUser(credentials)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo(BODY_401));
    }

    @Test
    @DisplayName("Login User With Incorrect Email")
    @Description("Логин клиента в системе с неправильным email")
    public void loginUserWithIncorrectEmail() {
        usersData.setEmail(usersData.getEmail()+2);
        UsersCredentials credentials = UsersCredentials.from(usersData);
        userClient.loginUser(credentials)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo(BODY_401));
    }

    @Test
    @DisplayName("Login User With Empty Body")
    @Description("Логин клиента в системе с пустым телом запроса")
    public void loginUserWithEmptyBody() {
        usersData.setEmail(null);
        usersData.setPassword(null);
        usersData.setName(null);
        UsersCredentials credentials = UsersCredentials.from(usersData);
        userClient.loginUser(credentials)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body(equalTo(BODY_401));
    }
}
