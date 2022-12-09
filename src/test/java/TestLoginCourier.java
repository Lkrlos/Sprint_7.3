import io.restassured.RestAssured;
import org.example.LoginCourier;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestLoginCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void loginCourier() {
        LoginCourier login = new LoginCourier("Karlos", "1234");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200).and()
                .assertThat().body("id", notNullValue());
    }

    @Test
    public void errorForBadPassword() {
        LoginCourier login = new LoginCourier("Karlos", "123432");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404).and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    public void errorForBadLoginName() {
        LoginCourier login = new LoginCourier("123Karlos", "1234");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404).and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    //Этот тест падает, так как ошибка в сервисе. Получаем 500 от сервера
    @Test
    public void NoRequiredFieldLoginCourier() {
        LoginCourier login = new LoginCourier("Karlos");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
