package com.gh.tradebot.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpbitTicker {
    private String market;
    private String coin;
    private BigDecimal trade_price;
}
