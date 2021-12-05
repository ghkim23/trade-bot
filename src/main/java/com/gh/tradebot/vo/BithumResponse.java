package com.gh.tradebot.vo;

import lombok.Data;

@Data
public class BithumResponse<T> {
    private String status;
    private T data;
}
