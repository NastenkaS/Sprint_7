package yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDERS_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
    }
}
