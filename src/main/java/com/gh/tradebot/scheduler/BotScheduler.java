package com.gh.tradebot.scheduler;

import com.gh.tradebot.entity.CoinQuote;
import com.gh.tradebot.service.BithumbService;
import com.gh.tradebot.vo.OrderBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BotScheduler {
    private static final Logger log = LogManager.getLogger(BotScheduler.class);

    public BotScheduler(BithumbService bithumbService){
        this.bithumbService = bithumbService;
    }

    private BithumbService bithumbService;

    @Scheduled(fixedDelay = 1000*60)
    public void insertQuote(){
        log.info("코인 시세 받아오기 시작");
        List<CoinQuote> orderBookList = bithumbService.insertCoinQuote("KRW");
        bithumbService.alarmBull(orderBookList);
    }

}
