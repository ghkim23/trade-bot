package com.gh.tradebot.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderBook {
    private String timestamp;
    private String order_currency;
    private String payment_currency;
    private List<Order> bids;
    private List<Order> asks;
}
