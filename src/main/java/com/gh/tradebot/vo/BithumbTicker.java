package com.gh.tradebot.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BithumbTicker {
    @JsonIgnore
    private String market;
    @JsonIgnore
    private String coin;
    private BigDecimal closing_price;
}
