package yandex;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import yandex.model.Courier;
import yandex.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseClient {
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера с ID: {courierId}")
    public Response deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    @Step("Авторизация курьера и получение ID")
    public int loginAndGetId(CourierCredentials credentials) {
        Response response = loginCourier(credentials);
        return response.then()
                .statusCode(200)
                .extract()
                .path("id");
    }
}
