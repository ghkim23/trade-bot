package com.gh.tradebot.scheduler;

import com.gh.tradebot.service.BithumbRestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BotScheduler {
    private static final Logger log = LogManager.getLogger(BotScheduler.class);

    public BotScheduler(BithumbRestService bithumbRestService){
        this.bithumbRestService = bithumbRestService;
    }

    private BithumbRestService bithumbRestService;

    @Scheduled(fixedDelay = 1000) // 배치 끝난 후 1초 뒤
    public void 테스트(){
        bithumbRestService.getTicker("BTC","KRW");
    }

}
