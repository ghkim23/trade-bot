package com.gh.tradebot.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient bithumbWebClient(){
        return WebClient.builder().baseUrl("https://api.bithumb.com").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(BithumbWebClientExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new BithumWebClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

    @Bean
    public WebClient upbitWebClient(){
        return WebClient.builder().baseUrl("https://api.upbit.com").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(BithumbWebClientExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new BithumWebClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

    @Bean
    public WebClient telegramWebClient(){
        return WebClient.builder().baseUrl("https://api.telegram.org/").filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().isError()) {
                return clientResponse.bodyToMono(TelegramWebClientExceptionDetails.class)
                        .flatMap(errorDetails -> Mono.error(new TelegramWebClientException(clientResponse.statusCode(), errorDetails)));
            }
            return Mono.just(clientResponse);
        })).build();
    }

}
