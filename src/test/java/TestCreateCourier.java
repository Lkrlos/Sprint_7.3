import io.restassured.RestAssured;
import org.example.CreateCourier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TestCreateCourier {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }


    @Test
    public void createdCourier() {
        CreateCourier courier = new CreateCourier("Karlitosina", "1234", "Karimka");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201).and()
                .body("ok", equalTo(true));

        given()
                .delete("/api/v1/courier/" + courier.getId());
    }

    @Test
    public void nonDoubleCreatedCourier() {
        CreateCourier courierOne = new CreateCourier("Karlos", "1234", "Karim");
        CreateCourier courierTwo = new CreateCourier("Karlos", "1234", "Karim");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierOne)
                .when()
                .post("/api/v1/courier");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courierTwo)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409).and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void NoRequiredFieldCreatedCourier() {
        CreateCourier courier = new CreateCourier("Karlosin1");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(400).and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
