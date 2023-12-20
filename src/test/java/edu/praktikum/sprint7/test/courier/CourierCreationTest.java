package edu.praktikum.sprint7.test.courier;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.sprint7.courier.CourierGenerator.randomCourier;
import static edu.praktikum.sprint7.models.CourierCred.fromCourier;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierCreationTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    CourierClient courierClient = new CourierClient();
    Courier courier = randomCourier();

    @Before
    public void setUp() {
        //базовый урл для запроса
        RestAssured.baseURI = BASE_URL;

    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера со случайными данными")
    public void newCourierCreationTest() {
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED,response.statusCode());
        Response loginResponse = courierClient.login(fromCourier(courier));
        assertEquals("Неверный статус код", SC_OK,loginResponse.statusCode());

    }

    @Test//нельзя создать двух одинаковых курьеров;
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Создание курьера со случайными данными и повторная его авторизация")
    public void oldCourierCreationTest(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED,response.statusCode());
        Response loginResponse = courierClient.login(fromCourier(courier));
        assertEquals("Неверный статус код", SC_OK,loginResponse.statusCode());
        response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CONFLICT,response.statusCode());
        assertEquals("Неверный текст ответа", "Этот логин уже используется. Попробуйте другой.", response.path("message"));

    }

    @Test//запрос возвращает правильный код ответа;
    @DisplayName("Создание курьера")
    @Description("Получение 201 ОК")
    public void correctResponseCourierCreationTest(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED,response.statusCode());
     }

    @Test //успешный запрос возвращает ok: true
    @DisplayName("Создание курьера")
    @Description("Получение ответа ok:true")
    public void answerCourierCreationTest(){
            Courier courier = randomCourier();
            CourierClient courierClient = new CourierClient();
            Response response = courierClient.create(courier);
            assertEquals("Неверный текст ответа", "ok: true",response.getBody().asString());
    }

    @Test//если создать пользователя с логином, который уже есть, возвращается ошибка.
    @DisplayName("Создание 2х курьеров")
    @Description("Создание второго курьера с login = login первого курьера")
    public void sameLoginCourierCreationTest(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED,response.statusCode());
        String courierLogin = courier.getLogin();
        Response loginResponse = courierClient.login(fromCourier(courier));
        assertEquals("Неверный статус код", SC_OK,loginResponse.statusCode());
        Courier.setLogin(courierLogin);
        response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CONFLICT,response.statusCode());

    }
    @Test//чтобы создать курьера, нужно передать в ручку все обязательные поля;
    @DisplayName("Создание курьера")
    @Description("Проверка на наличие всех передаваемых параметров")
    public void newCourierAllParametrsTest() {
        // Проверка передаваемых параметров
        assertNotNull("Логин курьера не должен быть null", courier.getLogin());
        assertNotNull("Пароль курьера не должен быть null", courier.getPassword());
        assertNotNull("Имя пользователя не должно быть пустым", courier.getFirstName());
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        assertEquals("Неверный статус код", SC_CREATED, response.statusCode());

    }
    @After
    public void tearDown() {
        courierClient.getIdCourier(courier);
        courierClient.remove(courier);

    }
}
