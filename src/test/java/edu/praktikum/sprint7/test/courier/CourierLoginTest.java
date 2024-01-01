package edu.praktikum.sprint7.test.courier;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.CourierCred;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static edu.praktikum.sprint7.courier.CourierGenerator.randomCourier;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CourierLoginTest {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private int id;
    CourierClient courierClient = new CourierClient();
    Courier courier = randomCourier();

    @Before
    public void setUp() {
        //базовый урл для запроса
        RestAssured.baseURI = BASE_URL;
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Вход курьера в систему с логином и паролем возможен")

    public void loginCourierWithLoginPasswordPossible(){
        Response response = courierClient.login(CourierCred.fromCourier(courier));
        assertNotNull(response.jsonPath().getString("id"));
        assertEquals("Неверный статус код ответа",SC_OK, response.statusCode());
    }

    @Test
    @DisplayName("Вход курьера в систему только с логином невозможен")
    public void loginCourierWithOnlyLoginImpossible(){
        CourierCred courierWithOnlyLogin = CourierCred.fromCourier(courier);
        courierWithOnlyLogin.setPassword("");

        Response response = courierClient.login(courierWithOnlyLogin);
        assertEquals("Неверный статус код ответа", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Неверный текст ответа","Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Вход курьера в систему только с паролем невозможен")
    public void loginCourierWithOnlyPasswordImpossible() {
        CourierCred courierWithOnlyPassword = CourierCred.fromCourier(courier);
        courierWithOnlyPassword.setLogin("");

        Response response = courierClient.login(courierWithOnlyPassword);
        assertEquals("Неверный статус код ответа",SC_BAD_REQUEST, response.statusCode());
        assertEquals("Неверный текст ответа","Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Вход курьера в систему без логина и пароля невозможен")
    public void loginCourierWithoutLoginPasswordImpossible() {
        CourierCred courierWithoutLoginPassword = CourierCred.fromCourier(courier);
        courierWithoutLoginPassword.setLogin("");
        courierWithoutLoginPassword.setPassword("");
        Response response = courierClient.login(courierWithoutLoginPassword);
        assertEquals("Неверный статус код ответа", SC_BAD_REQUEST, response.statusCode());
        assertEquals("Неверный текст ответа","Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Вход курьера в систему c неверным логином невозможен")
    public void loginCourierWithIncorrectLoginImpossible() {
        CourierCred courierWithIncorrectLogin = CourierCred.fromCourier(courier);
        courierWithIncorrectLogin.setPassword(randomAlphabetic(8));

        Response response = courierClient.login(courierWithIncorrectLogin);
        assertEquals("Неверный статус код ответа", SC_NOT_FOUND, response.statusCode());
        assertEquals("Неверный текст ответа","Учетная запись не найдена", response.path("message"));
    }

    @Test
    @DisplayName("Вход курьера в систему c неверным паролем невозможен")
    public void loginCourierWithIncorrectPasswordImpossible() {
        CourierCred courierWithIncorrectPassword = CourierCred.fromCourier(courier);
        courierWithIncorrectPassword.setPassword(randomAlphabetic(8));

        Response response = courierClient.login(courierWithIncorrectPassword);
        assertEquals("Неверный статус код ответа", SC_NOT_FOUND, response.statusCode());
        assertEquals("Неверный текст ответа","Учетная запись не найдена", response.path("message"));
    }

    public void tearDown() {
        courierClient.getIdCourier(courier);
        courierClient.remove(courier);
    }
}
