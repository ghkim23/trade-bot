package com.gh.tradebot.config.rest;

import lombok.Data;

@Data
public class TelegramWebClientExceptionDetails {
    private boolean ok;
    private int error_code;
    private String description;
}
