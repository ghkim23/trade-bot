package com.gh.tradebot.service;

import com.gh.tradebot.util.BithumUtil;
import com.gh.tradebot.vo.BithumResponse;
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

@Service
public class BithumbRestService {
    private static final Logger log = LogManager.getLogger(BithumbRestService.class);

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

    public void getOrderBook (String coin, String market){
        BithumResponse<OrderBook> response = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/orderbook/{coin}_{market}")
                        .build(coin,market)).retrieve()
                .bodyToMono(new ParameterizedTypeReference<BithumResponse<OrderBook>>() {
                }).block();
        log.info(response);
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

    public void getOrderBook(String coin, String market){
        String result = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/orderbook/{coin}/{market}")
                .build(coin,market)).retrieve().bodyToMono(String.class).block();

        log.error(result);
    }
}
