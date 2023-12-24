package edu.praktikum.sprint7.order;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

        private static final String CREATING_ORDER_URL = "/api/v1/orders";
        private static final String ACCEPT_ORDER_URL = "/api/v1/orders/accept/";
        private static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
        private static final String GET_ALL_ORDERS = "/api/v1/orders";
        private static final String FINISH_ORDER = "/api/v1/orders/finish/:id";
        private static final String GET_ORDER_BY_NUMBER = "/api/v1/orders/track";
        CourierClient courierClient = new CourierClient();

        @Step("Создать заказ")
        public Response create(Order order) {
            return given()
                    .log().all()
                    .header("Content-type", "application/json")
                    .body(order)
                    .and()
                    .post(CREATING_ORDER_URL);
        }

        @Step("Получить track заказа")
        public String getTrack(Order order) {
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .and()
                    .post(CREATING_ORDER_URL);
            return response.jsonPath().getString("track");
        }

        @Step("Отменить заказ")
        public Response cancel(Order order) {
            String track = getTrack(order);
            return given()
                    .header("Content-type", "application/json")
                    .body("{\"track\": " + track + " }")
                    .put(CANCEL_ORDER_URL);
        }
        @Step("Принять заказ")
        public void accept(Order order, Courier courier){
            String  courierId = courierClient.getIdCourier(courier);
            String id = getId(order);
            given()
                    .header("Content-type", "application/json")
                    .queryParam("courierId", courierId)
                    .pathParam("id", id)
                    .put(ACCEPT_ORDER_URL + "{id}");
        }

        @Step("Завершить заказ")
        public Response finished(Order order){
            String idOrders = getId(order);
            return given()
                    .header("Content-type", "application/json")
                    .pathParam("idOrders", idOrders)
                    .get(FINISH_ORDER + "{idOrders}");
        }

        @Step("Получить id заказа")
        public String getId(Order order) {
            String t = getTrack(order);
            Response response =
                    given()
                            .queryParam("t", t)
                            .get(GET_ORDER_BY_NUMBER);
            return response.jsonPath().getString("order.id");
        }
        @Step("Получить список всех заказов")
        public Response getAll(){
            return given()
                    .get(GET_ALL_ORDERS);
        }

        @Step("Получить список всех заказов курьера")
        public Response getAllCourierOrders(Courier courier){
            String courierId = courierClient.getIdCourier(courier);
            return given()
                    .queryParam("courierId", courierId)
                    .get(GET_ALL_ORDERS);
        }

        @Step("Получить список всех заказов курьера на станциях Бульвар Рокоссовского(1) или Черкизовская(2)")
        public Response getAllCourierOrdersAtStations(Courier courier){
            String courierId = courierClient.getIdCourier(courier);
            return given()
                    .queryParam("courierId", courierId)
                    .queryParam("nearestStation", "[\"1", "2\"]")
                    .get(GET_ALL_ORDERS);
        }
    }
