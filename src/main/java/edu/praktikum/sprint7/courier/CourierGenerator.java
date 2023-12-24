package edu.praktikum.sprint7.courier;

import com.github.javafaker.Faker;
import edu.praktikum.sprint7.models.Courier;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;


public class CourierGenerator {
    private static final Faker faker = new Faker();
    public static Courier randomCourier(){
        return new Courier()
                .withLogin(faker.name().username())
                .withPassword(faker.internet().password())
                .withFirstName(faker.name().firstName());
    }
}
