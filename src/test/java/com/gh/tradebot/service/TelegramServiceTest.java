package com.gh.tradebot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TelegramServiceTest {
    @Autowired
    private TelegramService telegramService;

    @Test
    void 텔레그램_메세지_보내기_테스트(){
        telegramService.sendMessageByTelegram("botToken","chatId","텍스트");
    }

}