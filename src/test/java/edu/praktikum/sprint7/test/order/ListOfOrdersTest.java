package edu.praktikum.sprint7.test.order;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.order.OrderClient;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import static edu.praktikum.sprint7.courier.CourierGenerator.randomCourier;
import static edu.praktikum.sprint7.order.OrderGenerator.randomOrder;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class ListOfOrdersTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    Courier courier = randomCourier();
    Order order = randomOrder();
    Order orderScooterTwo = randomOrder();
    CourierClient courierClient = new CourierClient();
    OrderClient orderScooterClient = new OrderClient();

    @Before
    public void setUp(){
        RestAssured.baseURI = BASE_URL;
        courierClient.create(courier);

        //Курьер принял заказ
        orderScooterClient.create(order);
        orderScooterClient.accept(order, courier);
        //Курьер принял и завершил заказ2
        orderScooterClient.create(orderScooterTwo);
        orderScooterClient.accept(orderScooterTwo, courier);
        orderScooterClient.finished(orderScooterTwo);
    }

    @Test
    @DisplayName("Получение списка всех заказов")
    public void checkListAll(){
        Response response = orderScooterClient.getAll();
        response
                .then()
                .assertThat()
                .body("orders", notNullValue());
        assertEquals("Неверный статус код ответа1", SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Получение списка всех активных и завершенных заказов курьера")
    public void checkListOfCourierOrders(){
        Response response = orderScooterClient.getAllCourierOrders(courier);
        response
                .then()
                .assertThat()
                .body("orders[1].courierId", equalTo(Integer.parseInt(courierClient.getIdCourier(courier))))
                .body("orders[2].courierId", equalTo(Integer.parseInt(courierClient.getIdCourier(courier))));
        assertEquals("Неверный статус код ответа2", SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Получение списка всех заказов курьера на станциях Бульвар Рокоссовского(1) или Черкизовская(2)")
    public void checkListOfCourierOrdersAtStation(){
        Order orderScooterStation1 = new Order()
                .withMetroStation("1");
        Order orderScooterStation2 = new Order()
                .withMetroStation("2");
        //Курьер принимает заказы со станций Бульвар Рокоссовского(1) и Черкизовская(2)
        orderScooterClient.accept(orderScooterStation1, courier);
        orderScooterClient.accept(orderScooterStation2, courier);

        Response response = orderScooterClient.getAllCourierOrdersAtStations(courier);
        response
                .then()
                .assertThat()
                .body("orders[1].metroStation", equalTo(1))
                .body("orders[2].metroStation", equalTo(2));
        assertEquals("Неверный статус код ответа3", SC_OK, response.statusCode());

        //Отмена заказов
        Response response1 = orderScooterClient.cancel(orderScooterStation1);
        assertEquals("Неверный статус код ответа4", SC_OK, response1.statusCode());
        Response response2 = orderScooterClient.cancel(orderScooterStation2);
        assertEquals("Неверный статус код ответа5", SC_OK, response2.statusCode());
    }

    @After
    public void tearDown(){
        courierClient.remove(courier);
        orderScooterClient.cancel(order);
    }


}
