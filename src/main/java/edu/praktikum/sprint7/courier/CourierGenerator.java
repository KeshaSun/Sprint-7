package edu.praktikum.sprint7.courier;

import edu.praktikum.sprint7.models.Courier;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class CourierGenerator {

    public static Courier randomCourier(){
        return new Courier()
                .withLogin(randomAlphabetic(8))
                .withPassword(randomAlphabetic(16))
                .withFirstName(randomAlphabetic(10));
    }
}
