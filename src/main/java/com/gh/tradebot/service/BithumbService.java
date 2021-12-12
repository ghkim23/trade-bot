package com.gh.tradebot.service;

import com.gh.tradebot.entity.CoinQuote;
import com.gh.tradebot.repository.CoinQuoteRepository;
import com.gh.tradebot.vo.OrderBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityListeners;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BithumbService {
    private static final Logger log = LogManager.getLogger(BithumbService.class);
    public BithumbService(BithumbRestService bithumbRestService, CoinQuoteRepository coinQuoteRepository
    , TelegramService telegramService){
        this.bithumbRestService = bithumbRestService;
        this.coinQuoteRepository = coinQuoteRepository;
        this.telegramService = telegramService;
    }
    private BithumbRestService bithumbRestService;
    private CoinQuoteRepository coinQuoteRepository;
    private TelegramService telegramService;

    public void alarmBull(List<CoinQuote> coinQuoteList){
        List<String> bullCoins = new ArrayList<>();
        for(CoinQuote coinQuote : coinQuoteList){
            List<CoinQuote> lastCoinQuotes = coinQuoteRepository.findTop2ByCoinOrderByIdDesc(coinQuote.getCoin());
            if(lastCoinQuotes.size()>1){
                BigDecimal price = lastCoinQuotes.get(0).getPrice();
                BigDecimal prevPrice = lastCoinQuotes.get(1).getPrice();
                BigDecimal percent = price.subtract(prevPrice).divide(prevPrice,4,2).multiply(BigDecimal.valueOf(100)).setScale(2,2);
                if(percent.compareTo(BigDecimal.valueOf(5))>0){
                    bullCoins.add(coinQuote.getCoin() + ": " + percent + "%" );
                }
            }
        }
        if(bullCoins.size()>0){
            log.info("떡상코인 발견 " + bullCoins);
            String message = "속보 떡상 중인 코인들\n";
            message +=bullCoins.stream().collect(Collectors.joining("\n"));
            telegramService.sendMessageByTelegram("","",message);
        }
    }

    public List<CoinQuote> insertCoinQuote(String market){
        List<OrderBook> orderBooks = bithumbRestService.getOrderBookAll(market);

        List<CoinQuote> coinQuoteList = new ArrayList<>();

        for(OrderBook orderBook : orderBooks){
            CoinQuote coinQuote = new CoinQuote();
            coinQuote.setCoin(orderBook.getOrder_currency());
            coinQuote.setMarket(market);
            coinQuote.setAmount(new BigDecimal(orderBook.getBids().get(0).getQuantity()));
            coinQuote.setPrice(new BigDecimal(orderBook.getBids().get(0).getPrice()));
            coinQuote.setType("buy");
            coinQuoteList.add(coinQuote);
        }

        coinQuoteRepository.saveAll(coinQuoteList);

        return coinQuoteList;
    }
}
