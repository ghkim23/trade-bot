package com.gh.tradebot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BithumbRestServiceTest {

    @Autowired
    private BithumbRestService bithumbRestService;

    @Test
    void Ticker테스트(){
        bithumbRestService.getTicker("BTC", "KRW");
    }

    @Test
    void 오더북테스트(){
        bithumbRestService.getOrderBook("BTC", "KRW");
    }
}