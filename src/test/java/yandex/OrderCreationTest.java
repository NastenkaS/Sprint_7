package yandex;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.Matchers.*;


@RunWith(Parameterized.class)
public class OrderCreationTest {
    private OrderClient orderClient;
    private final List<String> color;
    private final String testName;

    public OrderCreationTest(List<String> color, String testName) {
        this.color = color;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] getColorData() {
        return new Object[][]{
                {DataGenerator.getBlackColor(), "Создание заказа с цветом BLACK"},
                {DataGenerator.getGreyColor(), "Создание заказа с цветом GREY"},
                {DataGenerator.getBothColors(), "Создание заказа с обоими цветами"},
                {DataGenerator.getNoColor(), "Создание заказа без указания цвета"}
        };
    }

    @Before
    @Step("Подготовка к тесту")
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа с различными вариантами цвета")
    @Description("Проверка, что можно создать заказ с разными комбинациями цветов")
    public void orderCanBeCreatedWithColorTest() {
        Order order = DataGenerator.getRandomOrder(color);

        Response response = orderClient.createOrder(order);

        response.then()
                .assertThat()
                .statusCode(201)
                .body("track", notNullValue())
                .body("track", greaterThan(0));
    }
}
