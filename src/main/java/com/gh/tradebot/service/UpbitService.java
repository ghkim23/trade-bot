package com.gh.tradebot.service;

import com.gh.tradebot.entity.UpbitTicker;
import com.gh.tradebot.repository.UpbitTickerRepository;
import com.gh.tradebot.vo.MarketInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpbitService {

    private static final Logger log = LogManager.getLogger(UpbitService.class);
    public UpbitService(UpbitRestService restService, TelegramService telegramService
            , UpbitTickerRepository tickerRepository){
        this.restService = restService;
        this.telegramService = telegramService;
        this.tickerRepository = tickerRepository;
    }
    private UpbitRestService restService;
    private TelegramService telegramService;
    private UpbitTickerRepository tickerRepository;

    public List<com.gh.tradebot.entity.UpbitTicker> insertTicker(){
        List<MarketInfo> marketInfoList = restService.getMarketInfo();
        String tickerMarkets = marketInfoList.stream().map(x -> x.getMarket()).collect(Collectors.joining(","));
        List<com.gh.tradebot.vo.UpbitTicker> UpbitTickers = restService.getTicker(tickerMarkets);

        List<com.gh.tradebot.entity.UpbitTicker> entityTickers = new ArrayList<>();

        for(com.gh.tradebot.vo.UpbitTicker ticker : UpbitTickers){
            com.gh.tradebot.entity.UpbitTicker tickerEntity = new com.gh.tradebot.entity.UpbitTicker();
            tickerEntity.setCoin(ticker.getCoin());
            tickerEntity.setMarket(ticker.getMarket());
            tickerEntity.setPrice(ticker.getTrade_price());
            entityTickers.add(tickerEntity);
        }
        tickerRepository.saveAll(entityTickers);
        return  entityTickers;
    }

    public void alarmBull(List<com.gh.tradebot.entity.UpbitTicker> tickers,String botToken,String chatId){
        List<String> bullCoins = new ArrayList<>();
        for(com.gh.tradebot.entity.UpbitTicker ticker : tickers){
            List<com.gh.tradebot.entity.UpbitTicker> lastTickers = tickerRepository.findTop2ByMarketAndCoinOrderByIdDesc(ticker.getMarket(),ticker.getCoin());
            if(lastTickers.size()>1){
                BigDecimal price = lastTickers.get(0).getPrice();
                BigDecimal prevPrice = lastTickers.get(1).getPrice();
                if(price.compareTo(BigDecimal.ZERO) ==0 || prevPrice.compareTo(BigDecimal.ZERO) ==0) continue;
                BigDecimal percent = price.subtract(prevPrice).divide(prevPrice,8,2).multiply(BigDecimal.valueOf(100)).setScale(2,2);
                if(percent.compareTo(BigDecimal.valueOf(5))>0){
                    bullCoins.add("market : " + ticker.getMarket()+" coin : "+ ticker.getCoin() + ": " + percent + "%" );
                }
            }
        }
        if(bullCoins.size()>0){
            log.info("떡상코인 발견 " + bullCoins);
            String message = "속보 떡상 중인 코인들\n";
            message +=bullCoins.stream().collect(Collectors.joining("\n"));
            telegramService.sendMessageByTelegram(botToken,chatId,message);
        }
    }
}
