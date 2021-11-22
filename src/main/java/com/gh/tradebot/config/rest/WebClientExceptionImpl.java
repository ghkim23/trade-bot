package com.gh.tradebot.config.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@Getter
public class WebClientExceptionImpl extends WebClientException {
    private final HttpStatus status;
    private final WebClientExceptionDetails details;

    WebClientExceptionImpl(HttpStatus status, WebClientExceptionDetails details) {
        super(status.getReasonPhrase());
        this.status = status;
        this.details = details;
    }
}
