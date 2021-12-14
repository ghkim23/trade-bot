package com.gh.tradebot.service;

import com.gh.tradebot.entity.CoinQuote;
import com.gh.tradebot.repository.BithumbTickerRepository;
import com.gh.tradebot.repository.CoinQuoteRepository;
import com.gh.tradebot.vo.BithumbTicker;
import com.gh.tradebot.vo.OrderBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BithumbService {
    private static final Logger log = LogManager.getLogger(BithumbService.class);
    public BithumbService(BithumbRestService bithumbRestService, CoinQuoteRepository coinQuoteRepository
    , TelegramService telegramService, BithumbTickerRepository bithumbTickerRepository){
        this.bithumbRestService = bithumbRestService;
        this.coinQuoteRepository = coinQuoteRepository;
        this.telegramService = telegramService;
        this.bithumbTickerRepository = bithumbTickerRepository;
    }
    private BithumbRestService bithumbRestService;
    private CoinQuoteRepository coinQuoteRepository;
    private TelegramService telegramService;
    private BithumbTickerRepository bithumbTickerRepository;

    public void alarmBull(List<CoinQuote> coinQuoteList){
        List<String> bullCoins = new ArrayList<>();
        for(CoinQuote coinQuote : coinQuoteList){
            List<CoinQuote> lastCoinQuotes = coinQuoteRepository.findTop2ByCoinOrderByIdDesc(coinQuote.getCoin());
            if(lastCoinQuotes.size()>1){
                BigDecimal price = lastCoinQuotes.get(0).getPrice();
                BigDecimal prevPrice = lastCoinQuotes.get(1).getPrice();
                if(price.compareTo(BigDecimal.ZERO) ==0 || prevPrice.compareTo(BigDecimal.ZERO) ==0) continue;
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

    public List<com.gh.tradebot.entity.BithumbTicker> insertBithumbTicker(String market){
        List<BithumbTicker> bithumbTickers = bithumbRestService.getTickerAll(market);

        List<com.gh.tradebot.entity.BithumbTicker> bithumbEntityTickers = new ArrayList<>();

        for(BithumbTicker ticker : bithumbTickers){
            com.gh.tradebot.entity.BithumbTicker tickerEntity = new com.gh.tradebot.entity.BithumbTicker();
            tickerEntity.setCoin(ticker.getCoin());
            tickerEntity.setMarket(ticker.getMarket());
            tickerEntity.setPrice(ticker.getClosing_price());
            bithumbEntityTickers.add(tickerEntity);
        }
        bithumbTickerRepository.saveAll(bithumbEntityTickers);
        return  bithumbEntityTickers;
    }

    public void alarmBull(List<com.gh.tradebot.entity.BithumbTicker> tickers,String botToken,String chatId){
        List<String> bullCoins = new ArrayList<>();
        for(com.gh.tradebot.entity.BithumbTicker ticker : tickers){
            List<com.gh.tradebot.entity.BithumbTicker> lastTickers = bithumbTickerRepository.findTop2ByMarketAndCoinOrderByIdDesc(ticker.getMarket(),ticker.getCoin());
            if(lastTickers.size()>1){
                BigDecimal price = lastTickers.get(0).getPrice();
                BigDecimal prevPrice = lastTickers.get(1).getPrice();
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
