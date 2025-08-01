package com.example.mugbackend.gpt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
    public class GptConfig {

    @Value("${openapi.api.url}")
    private String apiUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiUrl)
                .build();
    }

}