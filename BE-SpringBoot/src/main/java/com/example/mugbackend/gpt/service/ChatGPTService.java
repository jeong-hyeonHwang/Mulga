package com.example.mugbackend.gpt.service;

import com.example.mugbackend.gpt.dto.ChatGPTRequest;
import com.example.mugbackend.gpt.dto.ChatGPTResponse;
import com.example.mugbackend.gpt.dto.PromptRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ChatGPTService {

    private final RestClient restClient;


    public ChatGPTService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Value("${openapi.api.key}")
    private String apiKey;

    @Value("${openapi.api.model}")
    private String model;

    @Value("${gpt.system.prompt}")
    private String systemPrompt;

    public String getChatResponse(String message) {

        if(message.equals("큐에서 메시지를 가져오지 못했습니다.")) {
            return "";
        }
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt(message)
                .build();

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(
                model,
                List.of(
                        new ChatGPTRequest.Message("system", systemPrompt),
                        new ChatGPTRequest.Message("user", promptRequest.prompt()))
        );

        ChatGPTResponse response = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatGPTRequest)
                .retrieve()
                .body(ChatGPTResponse.class);

        return response.choices().get(0).message().content();
    }

    public String getTheBestNoti(String message) {

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(
                model,
                List.of(new ChatGPTRequest.Message("user", message))
        );

        ChatGPTResponse response = restClient.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(chatGPTRequest)
                .retrieve()
                .body(ChatGPTResponse.class);

        return response.choices().get(0).message().content();
    }

}
