package com.gh.tradebot.config.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@Getter
public class TelegramWebClientException extends WebClientException {
    private final HttpStatus status;
    private final TelegramWebClientExceptionDetails details;

    TelegramWebClientException(HttpStatus status, TelegramWebClientExceptionDetails details) {
        super(status.getReasonPhrase());
        this.status = status;
        this.details = details;
    }
}
