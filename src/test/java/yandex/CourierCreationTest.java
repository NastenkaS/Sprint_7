package yandex;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreationTest {
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
    @DisplayName("Создание курьера с валидными данными")
    @Description("Проверка успешного создания курьера со всеми обязательными полями")
    public void courierCanBeCreatedTest() {
        courier = DataGenerator.getRandomCourier();

        Response response = courierClient.createCourier(courier);

        response.then()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(credentials);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать двух курьеров с одинаковым логином")
    public void cannotCreateDuplicateCourierTest() {
        courier = DataGenerator.getRandomCourier();

        Response firstResponse = courierClient.createCourier(courier);
        firstResponse.then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(credentials);

        Response secondResponse = courierClient.createCourier(courier);

        secondResponse.then()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что без обязательного поля login создать курьера нельзя")
    public void cannotCreateCourierWithoutLoginTest() {
        courier = new Courier(null, "password123", "TestName");

        Response response = courierClient.createCourier(courier);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что без обязательного поля password создать курьера нельзя")
    public void cannotCreateCourierWithoutPasswordTest() {
        courier = new Courier("testLogin123", null, "TestName");

        Response response = courierClient.createCourier(courier);

        response.then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без firstName")
    @Description("Проверка, что можно создать курьера без необязательного поля firstName")
    public void canCreateCourierWithoutFirstNameTest() {
        courier = new Courier("testLogin" + System.currentTimeMillis(), "password123", null);

        Response response = courierClient.createCourier(courier);

        response.then()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));

        // Сохраняем ID для удаления
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(credentials);
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    @Description("Проверка ошибки при создании курьера с уже существующим логином")
    public void cannotCreateCourierWithExistingLoginTest() {
        courier = DataGenerator.getRandomCourier();

        courierClient.createCourier(courier).then().statusCode(201);

        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierId = courierClient.loginAndGetId(credentials);

        Courier duplicateCourier = new Courier(courier.getLogin(), "differentPassword", "DifferentName");
        Response response = courierClient.createCourier(duplicateCourier);

        response.then()
                .assertThat()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
