package edu.praktikum.sprint7.courier;

import edu.praktikum.sprint7.models.Courier;
import edu.praktikum.sprint7.models.CourierCred;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String COURIER_URL = "/api/v1/courier";
    private static final String COURIER_LOGIN_URL = "/api/v1/courier/login";
    private static final String COURIER_DELETE_URL = "/api/v1/courier/";

    @Step("Создание курьера {courier}")
    public Response create(Courier courier){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER_URL);
    }
    @Step("Send POST request to /api/v1/courier/login - получение id курьера")
    public String getIdCourier(Courier courier){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .post(COURIER_LOGIN_URL);
        return response.jsonPath().getString("id");
    }
    @Step("Авторизация курьером {courierCred}")
    public Response login(CourierCred courierCred){
        return given()
                .log().all()
                .header("Content-type", "application/json")
                .and()
                .body(courierCred)
                .when()
                .post(COURIER_LOGIN_URL);
    }
    @Step("Send DELETE request to /api/v1/courier/:id - удаление курьера")
    public void remove(Courier courier){
        String id = getIdCourier(courier);
        given()
                .header("Content-type", "application/json")
                .and()
                .pathParam("id", id)
                .body("{\"id\":\"" + id + "\"}")
                .when()
                .delete(COURIER_DELETE_URL + "{id}");
    }
}

