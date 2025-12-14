package yandex;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class CourierLoginTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    @Step("Подготовка к тесту")
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    @Step("Очистка тестовых данных")
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Авторизация курьера с валидными данными")
    @Description("Проверка успешной авторизации курьера с корректными логином и паролем")
    public void courierCanLoginTest() {
        courier = DataGenerator.getRandomCourier();
        courierClient.createCourier(courier).then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));

        courierId = response.then().extract().path("id");
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка, что без обязательного поля login авторизация невозможна")
    public void cannotLoginWithoutLoginTest() {
        CourierCredentials credentials = new CourierCredentials(null, "password123");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка, что без обязательного поля password авторизация невозможна")
    public void cannotLoginWithoutPasswordTest() {
        CourierCredentials credentials = new CourierCredentials("testLogin", null);
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    @Description("Проверка ошибки при авторизации с несуществующим логином")
    public void cannotLoginWithWrongLoginTest() {
        courier = DataGenerator.getRandomCourier();
        courierClient.createCourier(courier).then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials("wrongLogin123", courier.getPassword());
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

        CourierCredentials correctCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(correctCredentials);
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    @Description("Проверка ошибки при авторизации с неправильным паролем")
    public void cannotLoginWithWrongPasswordTest() {
        courier = DataGenerator.getRandomCourier();
        courierClient.createCourier(courier).then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrongPassword123");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));

        CourierCredentials correctCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(correctCredentials);
    }

    @Test
    @DisplayName("Авторизация несуществующего пользователя")
    @Description("Проверка ошибки при авторизации несуществующего пользователя")
    public void cannotLoginNonExistentUserTest() {
        CourierCredentials credentials = new CourierCredentials("nonExistentUser" + System.currentTimeMillis(), "somePassword");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешная авторизация возвращает ID")
    @Description("Проверка, что при успешной авторизации возвращается ID курьера")
    public void loginReturnsIdTest() {
        courier = DataGenerator.getRandomCourier();
        courierClient.createCourier(courier).then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", isA(Integer.class))
                .body("id", greaterThan(0));

        courierId = response.then().extract().path("id");
    }
}
