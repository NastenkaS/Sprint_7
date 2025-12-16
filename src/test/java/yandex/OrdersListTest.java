package yandex;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class OrdersListTest {
    private OrderClient orderClient;

    @Before
    @Step("Подготовка к тесту")
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в теле ответа возвращается список заказов")
    public void getOrdersListTest() {
        Response response = orderClient.getOrdersList();

        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("orders", isA(java.util.List.class))
                .body("orders", not(empty()));
    }

    @Test
    @DisplayName("Проверка структуры ответа списка заказов")
    @Description("Проверка, что ответ содержит необходимые поля")
    public void ordersListHasCorrectStructureTest() {
        Response response = orderClient.getOrdersList();

        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("pageInfo", notNullValue())
                .body("pageInfo.page", notNullValue())
                .body("pageInfo.total", notNullValue())
                .body("availableStations", notNullValue());
    }

    @Test
    @DisplayName("Проверка, что список заказов не пустой")
    @Description("Проверка, что возвращается хотя бы один заказ")
    public void ordersListIsNotEmptyTest() {
        Response response = orderClient.getOrdersList();

        response.then()
                .assertThat()
                .statusCode(SC_OK)
                .body("orders", hasSize(greaterThan(0)));
    }
}
