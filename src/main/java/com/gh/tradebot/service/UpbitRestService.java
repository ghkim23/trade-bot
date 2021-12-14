package com.gh.tradebot.service;

import com.gh.tradebot.vo.MarketInfo;
import com.gh.tradebot.vo.UpbitTicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class UpbitRestService {
    private static final Logger log = LogManager.getLogger(UpbitRestService.class);

    public UpbitRestService(WebClient upbitWebClient){
        this.upbitWebClient = upbitWebClient;
    }

    private WebClient upbitWebClient;


    public List<MarketInfo> getMarketInfo(){
        List<MarketInfo> marketInfoList= upbitWebClient.get().uri(uriBuilder -> uriBuilder.path("/v1/market/all")
                .queryParam("isDetails","false").build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<MarketInfo>>() {}).block();
        return marketInfoList;
    }

    public List<UpbitTicker> getTicker(String markets){
        List<UpbitTicker> tickers= upbitWebClient.get().uri(uriBuilder -> uriBuilder.path("/v1/ticker")
                .queryParam("markets",markets).build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<UpbitTicker>>() {}).block();

        for(UpbitTicker ticker : tickers){
            ticker.setCoin(ticker.getMarket().split("-")[1]);
            ticker.setMarket(ticker.getMarket().split("-")[0]);
        }

        return tickers;
    }
}

