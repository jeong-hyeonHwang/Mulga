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

    public String getChatResponse(PromptRequest promptRequest) {
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


        // TODO: 출력문 삭제
        System.out.println("GPT API 응답: " +  response.choices().get(0).message().content());


        return response.choices().get(0).message().content();
    }

}
