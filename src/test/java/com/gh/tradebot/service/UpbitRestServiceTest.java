package com.gh.tradebot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UpbitRestServiceTest {
    private static final Logger log = LogManager.getLogger(UpbitRestServiceTest.class);

    @Autowired
    private UpbitRestService upbitRestService;

    @Test
    void 마켓정보테스트(){
        upbitRestService.getMarketInfo();
    }

    @Test
    void Ticker(){
        upbitRestService.getTicker("KRW-BTC,KRW-ETH");
    }

}