package com.gh.tradebot.service;

import com.gh.tradebot.vo.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    public TradeService(BithumbRestService bithumbRestService){
        this.bithumbRestService = bithumbRestService;
    }

    private BithumbRestService bithumbRestService;



    /**
     * 원화 마켓에서 매도가격이 BTC 마켓에서 매수가격보다 작은지 확인 하는 기능
     * BTC 마켓 가격 계산은 BTC 매수가격 기준
     */
    public void isKRWMarketSellLowerThanBTCMarketBuy(String coin){
        bithumbRestService.getOrderBook(coin,"KRW");

    }



}
