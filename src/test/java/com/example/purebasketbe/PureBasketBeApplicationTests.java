package com.example.purebasketbe;

import com.example.purebasketbe.domain.product.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class PureBasketBeApplicationTests {

    @Test
    void contextLoads() {
        Event event = Event.DISCOUNT;

        System.out.println(event.rate(0.50));
    }

}
