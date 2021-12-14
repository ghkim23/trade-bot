package com.gh.tradebot.scheduler;

import com.gh.tradebot.entity.BithumbTicker;
import com.gh.tradebot.entity.UpbitTicker;
import com.gh.tradebot.service.BithumbService;
import com.gh.tradebot.service.UpbitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotScheduler {
    private static final Logger log = LogManager.getLogger(BotScheduler.class);

    public BotScheduler(BithumbService bithumbService, UpbitService upbitService){
        this.bithumbService = bithumbService;
        this.upbitService = upbitService;
    }

    private BithumbService bithumbService;
    private UpbitService upbitService;

    @Scheduled(fixedDelay = 1000*60)
    public void insertBithumbTicker(){
        log.info("빗썸 코인 시세 받아오기 시작");
        List<BithumbTicker> tickers = bithumbService.insertBithumbTicker("KRW");
        bithumbService.alarmBull(tickers,"","");
    }

    @Scheduled(fixedDelay = 1000*60)
    public void insertUpbitTicker(){
        log.info("업비트 코인 시세 받아오기 시작");
        List<UpbitTicker> tickers = upbitService.insertTicker();
        upbitService.alarmBull(tickers,"","");
    }

}
