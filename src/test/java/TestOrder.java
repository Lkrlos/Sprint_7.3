import io.restassured.RestAssured;
import org.example.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class TestOrder {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    List<String> color;
    public TestOrder(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColor() {
        return new Object[][] {
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GRAY")},
                {List.of("")},
        };
    }

    @Test
    public void getOrder() {
        Order order = new Order("Karim", "Bushenaki", "Брянск, Ромашина", "4",
                "+7 800 355 35 35", 5, "2020-12-06", "Saske, come back to Konoha", color);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then().statusCode(201).and()
                .assertThat().body("track", notNullValue());
    }

    @Test
    public void getListOrder() {
        given()
                .get("/api/v1/orders")
                .then().statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }
}

