package users;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserClient extends RestAssuredUser{

    @Step("Создание клиента")
    public ValidatableResponse createUser(UsersData usersData) {
        return reqSpec
                .body(usersData)
                .when()
                .post(CREATE_USER)
                .then().log().all();
    }

    @Step("Удаление клиента")
    public void deleteUser(String token){
        reqSpec
                .auth().oauth2(token)
                .when()
                .delete(UPDATE_DATA)
                .then().log().all();
    }

    @Step("Логин клиента в системе")
    public ValidatableResponse loginUser(UsersCredentials credentials) {
        return reqSpec
                .body(credentials)
                .when()
                .post(LOGIN)
                .then().log().all();
    }
}
