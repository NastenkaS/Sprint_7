package yandex;

import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ApiConnectionTest {

    @Test
    public void checkApiAvailability() {
        String[] urls = {
            "http://qa-scooter.praktikum-services.ru/api/v1/orders",
            "https://qa-scooter.praktikum-services.ru/api/v1/orders"
        };

        for (String url : urls) {
            System.out.println("\n=== Проверка URL: " + url + " ===");
            try {
                Response response = given()
                    .relaxedHTTPSValidation()
                    .when()
                    .get(url);
                
                System.out.println("Статус код: " + response.getStatusCode());
                System.out.println("Тело ответа: " + response.getBody().asString());
                System.out.println("Headers: " + response.getHeaders());
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
