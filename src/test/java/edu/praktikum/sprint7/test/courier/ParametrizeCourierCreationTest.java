package edu.praktikum.sprint7.test.courier;

import edu.praktikum.sprint7.courier.CourierClient;
import edu.praktikum.sprint7.models.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ParametrizeCourierCreationTest {
    private final String login;
    private final String password;
    private final String firstName;
    CourierClient courierClient = new CourierClient();

    public ParametrizeCourierCreationTest(
            String login,
            String password,
            String firstName

    ) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getSumData() {
        return new Object[][]{
                {randomAlphabetic(8), randomAlphabetic(16), null}, //присутствует, присутствует, отсутствует
                {randomAlphabetic(8), null, randomAlphabetic(10)}, //присутствует, отсутствует, присутствует
                {randomAlphabetic(8), null, null},                      //присутствует, отсутствует, отсутствует
                {null, randomAlphabetic(16), randomAlphabetic(10)}, //отсутствует, присутствует, присутствует
                {null, randomAlphabetic(16), null},                      //отсутствует, присутствует, присутствует
                {null, null, null}
        };
    }

    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

    public Courier CourierCreation() {
        return new Courier()
                .withLogin(login)
                .withPassword(password)
                .withFirstName(firstName);
    }

    @Before
    public void setUp() {
        //базовый урл для запроса
        RestAssured.baseURI = BASE_URL;

    }

    @Test//если одного из полей нет, запрос возвращает ошибку;
    @DisplayName("Создание курьеров с отсутствующими данными")
    @Description("Создание курьера со случайными данными и повторная его авторизация")
    public void newCourierCreationFieldsTest() {
        Response response = courierClient.create(CourierCreation());
        if (response.statusCode() == HttpStatus.SC_CREATED) {
            assertEquals("Неверный статус код", HttpStatus.SC_CREATED, response.statusCode());
            courierClient.getIdCourier(CourierCreation());
            courierClient.remove(CourierCreation());
        } else {
            assertEquals("Неверный статус код", HttpStatus.SC_BAD_REQUEST, response.statusCode());
        }
    }
}
