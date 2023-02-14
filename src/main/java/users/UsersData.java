package users;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersData {
    private String email;
    private String password;
    private String name;

    public UsersData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Step("Создание рандомных значений email,password,name")
    public static UsersData getRandom() {
        String email = RandomStringUtils.randomAlphanumeric(6) +"@mail.ru";
        String password = RandomStringUtils.randomAlphanumeric(6);
        String name = RandomStringUtils.randomAlphabetic(8);
        return new UsersData(email,password,name);
    }
}
