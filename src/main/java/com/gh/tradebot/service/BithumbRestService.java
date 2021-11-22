package com.gh.tradebot.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BithumbRestService {
    private static final Logger log = LogManager.getLogger(BithumbRestService.class);

    public BithumbRestService(WebClient bithumbWebClient){
        this.bithumbWebClient = bithumbWebClient;
    }

    private WebClient bithumbWebClient;

    public void getTicker(String coin, String market){
        String result = bithumbWebClient.get().uri(uriBuilder -> uriBuilder.path("/public/ticker/{coin}/{market}")
                .build(coin,market)).retrieve().bodyToMono(String.class).block();

        log.error(result);
    }
}
