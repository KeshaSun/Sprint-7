package edu.praktikum.sprint7.test.courier;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static edu.praktikum.sprint7.courier.CourierGenerator.randomCourier;
import static edu.praktikum.sprint7.models.CourierCred.fromCourier;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParametrizeCourierCreationTest {
    private  final String login;
    private final String password;
    private final String firstName;
    private final String status;
    CourierClient courierClient = new CourierClient();
    Courier courier = randomCourier();
    public ParametrizeCourierCreationTest(
            String login,
            String password,
            String firstName,
            String status

    )
    {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.status = status;
    }

    @Parameterized.Parameters
    public static Object[][] getSumData() {
        return new Object[][] {
                {randomAlphabetic(8),randomAlphabetic(8),null,SC_BAD_REQUEST}, //присутствует, присутствует, отсутствует
                {randomAlphabetic(8),null,randomAlphabetic(8),SC_BAD_REQUEST}, //присутствует, отсутствует, присутствует
                {randomAlphabetic(8),null,null,SC_BAD_REQUEST},                      //присутствует, отсутствует, отсутствует
                {null,randomAlphabetic(8),randomAlphabetic(8),SC_BAD_REQUEST}, //отсутствует, присутствует, присутствует
                {null,randomAlphabetic(8),null,SC_BAD_REQUEST},                      //отсутствует, присутствует, присутствует
                {null,null,null,SC_BAD_REQUEST},
                {randomAlphabetic(8),randomAlphabetic(8),randomAlphabetic(8)}
        };
    }

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";
    private int id;

    @Before
    public void setUp() {
        //базовый урл для запроса
        RestAssured.baseURI = BASE_URL;

    }

    @Test//если одного из полей нет, запрос возвращает ошибку;
    @DisplayName("Создание курьеров с отсутствующими данными")
    @Description("Создание курьера со случайными данными и повторная его авторизация")
    public void  newCourierCreationFieldsTest(){
        Courier courier = randomCourier();
        CourierClient courierClient = new CourierClient();
        Response response = courierClient.create(courier);
        Courier.setLogin(login);
        Courier.setPassword(password);
        courier.setFirstName(firstName);
        assertEquals("Неверный статус код",status,response.statusCode());

    }

    @After
    public void tearDown() {
        courierClient.getIdCourier(courier);
        courierClient.remove(courier);

    }
}
