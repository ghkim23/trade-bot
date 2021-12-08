package com.gh.tradebot.config.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientException;

@Getter
public class BithumbWebClientExceptionImpl extends WebClientException {
    private final HttpStatus status;
    private final BithumbWebClientExceptionDetails details;

    BithumbWebClientExceptionImpl(HttpStatus status, BithumbWebClientExceptionDetails details) {
        super(status.getReasonPhrase());
        this.status = status;
        this.details = details;
    }
}
