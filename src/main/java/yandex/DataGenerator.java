package yandex;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataGenerator {
    private static final Random random = new Random();

    public static Courier getRandomCourier() {
        String randomString = String.valueOf(System.currentTimeMillis());
        return new Courier(
                "courier_" + randomString,
                "password_" + randomString,
                "FirstName_" + randomString
        );
    }

    public static Order getRandomOrder(List<String> color) {
        return new Order(
                "Иван",
                "Иванов",
                "ул. Ленина, д. 1",
                "Сокольники",
                "+79991234567",
                5,
                "2024-12-20",
                "Тестовый заказ",
                color
        );
    }

    public static List<String> getBlackColor() {
        return Arrays.asList("BLACK");
    }

    public static List<String> getGreyColor() {
        return Arrays.asList("GREY");
    }

    public static List<String> getBothColors() {
        return Arrays.asList("BLACK", "GREY");
    }

    public static List<String> getNoColor() {
        return Arrays.asList();
    }
}
