package edu.praktikum.sprint7.order;

import edu.praktikum.sprint7.models.Order;
import static edu.praktikum.sprint7.utils.Utils.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;


public class OrderGenerator {
    public static Order randomOrder(){
        return new Order()
                .withFirstName(randomAlphabetic(5))
                .withLastName(randomAlphabetic(5))
                .withAddress(randomAlphabetic(5))
                .withMetroStation(randomAlphabetic(5))
                .withPhone(randomAlphabetic(5))
                .withRentTime(getRandomNumber(1,3))
                .withDeliveryDate(getRandomDeliveryDate())
                .withComment(randomString(5))
                .withColor(new String[]{randomString(3), randomString(3)});
    }
}
