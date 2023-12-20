package edu.praktikum.sprint7.courier.order;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public static class OrderScooterClient {

        private static final String CREATING_ORDER_URL = "/api/v1/orders";
        private static final String ACCEPT_ORDER_URL = "/api/v1/orders/accept/";
        private static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
        private static final String GET_ALL_ORDERS = "/api/v1/orders";
        private static final String FINISH_ORDER = "/api/v1/orders/finish/:id";
        private static final String GET_ORDER_BY_NUMBER = "/api/v1/orders/track";
        CourierClient courierClient = new CourierClient();

        @Step("Send POST request to /api/v1/orders - создание заказа")
        public Response create(Order order) {
            return given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .and()
                    .post(CREATING_ORDER_URL);
        }

        @Step("Send POST request to /api/v1/orders - получение track заказа")
        public String getTrack(Order order) {
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .and()
                    .post(CREATING_ORDER_URL);
            return response.jsonPath().getString("track");
        }

        @Step("Send PUT request to /api/v1/orders/cancel - отмена заказа")
        public Response cancel(Order order) {
            String track = getTrack(order);
            return given()
                    .header("Content-type", "application/json")
                    .body("{\"track\": " + track + " }")
                    .put(CANCEL_ORDER_URL);
        }

    }
}