package edu.praktikum.sprint7.test.order;

import edu.praktikum.sprint7.courier.order.OrderClient;
import edu.praktikum.sprint7.models.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static edu.praktikum.sprint7.courier.order.OrderGenerator.randomOrder;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreatingOrderTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private final String[] color;
    OrderClient orderClient = new OrderClient();
    Order order = randomOrder();
    public CreatingOrderTest(String[] color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getInformationAboutColor(){
        return new Object[][]{
                {new String[]{"BLACK", "GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{}}
        };
    }

    @Before
    public void setUp() {
        //базовый урл для запроса
        RestAssured.baseURI = BASE_URL;

    }

    @Test
    @DisplayName("Заказ можно указать различные комбинации цветов BLACK и GREY")
    public void checkSetColorBlackGray(){
        order.setColor(color);
        Response response = orderClient.create(order);
        assertNotNull(response.jsonPath().getString("track"));
        assertEquals("Неверный статус код ответа", SC_CREATED, response.statusCode());
    }

    @After
    public void tearDown(){
        orderClient.cancel(order);
    }

}
