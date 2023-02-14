package users;

import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import java.util.Locale;

public class RestAssuredUser {
    Faker faker = new Faker(Locale.US);

    // URL
    protected final String URL ="https://stellarburgers.nomoreparties.site/api";
    protected final String  CREATE_USER = "/auth/register"; // создание клиента
    protected final String  LOGIN = "/auth/login"; // логин в системе
    protected final String  LOGOUT = "/auth/logout";
    protected final String  UPDATE_DATA = "/auth/user"; //изменение данных
    protected final String  ORDERS = "/orders"; // создание заказа

    //DATA
    protected final String randomName = faker.name().firstName();
    protected final String randomEmail = faker.name().username()+"@gmail.com";
    protected final String newUserName = "{\"name\":\"" + randomName + "\"}";
    protected final String anotherEmail = "{\"email\":\"" + randomEmail + "\"}";
    protected final String ingredients = "{\"ingredients\":[\"61c0c5a71d1f82001bdaaa6d\"]}";
    protected final String wrong_ingredients = "{\"ingredients\":[\"61c0c5\"]}";
    protected final String empty_ingredients = "{\"ingredients\":[]}";

    //BODY RESPONSE
    protected final String BODY_401 = "{\"success\":false,\"message\":\"email or password are incorrect\"}";
    protected final String BODY_403 = "{\"success\":false,\"message\":\"Email, password and name are required fields\"}";
    protected final String UNAUTH_LOGIN = "{\"success\":false,\"message\":\"You should be authorised\"}";
    protected final String ERROR_EXIST_EMAIL = "{\"success\":false,\"message\":\"User with such email already exists\"}";
    protected final String ORDER_EMPTY_BODY = "{\"success\":false,\"message\":\"Ingredient ids must be provided\"}";


    protected final RequestSpecification reqSpec = given()
            .header("Content-type","application/json")
            .baseUri(URL);
}
