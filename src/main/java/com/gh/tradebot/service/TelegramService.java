package com.gh.tradebot.service;

import com.gh.tradebot.vo.TelegramResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TelegramService {
    private static final Logger log = LogManager.getLogger(TelegramService.class);

    public TelegramService(WebClient telegramWebClient){
        this.telegramWebClient =telegramWebClient;
    }


    private WebClient telegramWebClient;

    public TelegramResponse sendMessageByTelegram(String botToken, String chatId, String text){
        return telegramWebClient.get().uri(uriBuilder -> uriBuilder.path("/bot{botToken}/sendMessage")
                .queryParam("chat_id",chatId)
                .queryParam("text",text)
                .build(botToken)).retrieve()
                .bodyToMono(TelegramResponse.class).block();
    }
}
