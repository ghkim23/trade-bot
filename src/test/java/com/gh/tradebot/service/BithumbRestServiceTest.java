package com.gh.tradebot.service;

import com.gh.tradebot.config.rest.BithumWebClientException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BithumbRestServiceTest {
    private static final Logger log = LogManager.getLogger(BithumbRestServiceTest.class);

    @Autowired
    private BithumbRestService bithumbRestService;

    @Test
    void Ticker테스트(){
        bithumbRestService.getTicker("BNT", "KRW");
    }

    @Test
    void 오더북테스트(){
        bithumbRestService.getOrderBook("BNT", "KRW");
    }

    @Test
    void 회원정보테스트(){
        try {
            bithumbRestService.getAccountInfo("apikey", "seccrt","BNT", "KRW");
        }catch (BithumWebClientException e){
          log.error(e);
          log.error(e.getDetails());
        } catch (Exception e){
            log.error(e);
        }
    }

}