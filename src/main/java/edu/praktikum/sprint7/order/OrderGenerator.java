package edu.praktikum.sprint7.order;

import com.github.javafaker.Faker;
import edu.praktikum.sprint7.models.Order;
import static edu.praktikum.sprint7.utils.Utils.*;


public class OrderGenerator {
    private static final Faker faker = new Faker();

    public static Order randomOrder(){
        return new Order()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withAddress(faker.address().streetAddress())
                .withMetroStation(faker.address().city())
                .withPhone(faker.phoneNumber().cellPhone())
                .withRentTime(getRandomNumber(1,3))
                .withDeliveryDate(getRandomDeliveryDate())
                .withComment(faker.lorem().characters(5))
                .withColor(new String[]{randomString(3), randomString(3)});
    }
}
