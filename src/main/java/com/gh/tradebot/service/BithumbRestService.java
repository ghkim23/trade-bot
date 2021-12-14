package com.gh.tradebot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gh.tradebot.util.BithumUtil;
import com.gh.tradebot.vo.BithumResponse;
import com.gh.tradebot.vo.BithumbTicker;
import com.gh.tradebot.vo.OrderBook;
import com.gh.tradebot.vo.Ticker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BithumbRestService {
    private static final Logger log = LogManager.getLogger(BithumbRestService.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public BithumbRestService(WebClient bithumbWebClient){
        this.bithumbWebClient = bithumbWebClient;
    }

    private WebClient bithumbWebClient;

    public void getTicker(String coin, String market){
        BithumResponse<Ticker> response = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/ticker/{coin}_{market}")
                .build(coin,market)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<BithumResponse<Ticker>>() {
                }).block();
        log.info(response);
    }

    public List<BithumbTicker> getTickerAll (String market){
        BithumResponse<Map<String, Object>> response = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/ticker/ALL_{market}")
                .build(market)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<BithumResponse<Map<String, Object>>>() {
                }).block();
        ArrayList<BithumbTicker> tickers = new ArrayList<>();
        for(String key: response.getData().keySet()){
            if(key.equalsIgnoreCase("date")) continue;
            BithumbTicker ticker = mapper.convertValue(response.getData().get(key),BithumbTicker.class);
            ticker.setMarket(market);
            ticker.setCoin(key);
            tickers.add(ticker);
        }
        return tickers;
    }

    public OrderBook getOrderBook (String coin, String market){
        BithumResponse<OrderBook> response = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/orderbook/{coin}_{market}")
                        .build(coin,market)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<BithumResponse<OrderBook>>() {
                }).block();
        return response.getData();
    }

    public List<OrderBook> getOrderBookAll (String market){
        BithumResponse<Map<String, Object>> response = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/orderbook/ALL_{market}")
                .queryParam("count","1")
                .build(market)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<BithumResponse<Map<String, Object>>>() {
                }).block();
        ArrayList<OrderBook> orderBooks = new ArrayList<>();
        for(String key: response.getData().keySet()){
            if(key.equalsIgnoreCase("payment_currency") || key.equalsIgnoreCase("timestamp")) continue;
            OrderBook orderBook = mapper.convertValue(response.getData().get(key),OrderBook.class);
            orderBooks.add(orderBook);
        }
        return orderBooks;
    }

    public void getAccountInfo (String apikey, String secret, String coin, String market) throws Exception{
        final String endPoint = "/info/account";
        final MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("order_currency",coin);
        body.add("payment_currency",market);
        MultiValueMap<String, String> headers = BithumUtil.getHttpHeaders(endPoint, body, apikey, secret);
        String response = bithumbWebClient.post()
                .uri(uriBuilder -> uriBuilder.path(endPoint).build())
                .headers(h -> h.addAll(headers))
                .body(BodyInserters.fromFormData(body))
                .retrieve()
                .bodyToMono(String.class).block();
        log.info(response);
    }
}
