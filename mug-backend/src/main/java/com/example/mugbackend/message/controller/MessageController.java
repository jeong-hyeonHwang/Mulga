package com.example.mugbackend.message.controller;


import com.example.mugbackend.gpt.dto.PromptRequest;
import com.example.mugbackend.gpt.service.ChatGPTService;
import com.example.mugbackend.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final ChatGPTService chatGPTService;
    public MessageController(MessageService messageService, ChatGPTService chatGPTService) {
        this.messageService = messageService;
        this.chatGPTService = chatGPTService;
    }

    @GetMapping("/rabbit-message")
    public ResponseEntity<String> rabbitMessage() {

        List<String> messages = messageService.fetchMessages(1);

        if (messages.isEmpty()) {
            return ResponseEntity.ok("큐에 메시지 없음");
        }


        String gptMessage = chatGPTService.getChatResponse(messages.get(0));
        System.out.println("메시지 원본: " + messages.get(0));
        System.out.println("GPT API 응답: " + gptMessage);
        return ResponseEntity.ok(gptMessage);
    }

}
