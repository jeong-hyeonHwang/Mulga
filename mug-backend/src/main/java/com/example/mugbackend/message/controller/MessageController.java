package com.example.mugbackend.message.controller;


import com.example.mugbackend.gpt.dto.PromptRequest;
import com.example.mugbackend.gpt.service.ChatGPTService;
import com.example.mugbackend.message.dto.MessageRequestDto;
import com.example.mugbackend.message.service.MessageService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/rabbit-message")
    public ResponseEntity<String> rabbitMessage(@RequestBody MessageRequestDto requestDto) {

        System.out.println("컨트롤러: 진입");

        List<String> messages = messageService.fetchMessages(requestDto.getCount());

        System.out.println("컨트롤러: service 끝남." + messages.get(0));



        if (messages.isEmpty()) {
            return ResponseEntity.ok("큐에 메시지 없음");
        }

        // 첫 번째 메시지만 gpt에 전달
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt(messages.get(0))
                .build();

        String gptMessage = chatGPTService.getChatResponse(promptRequest);
        return ResponseEntity.ok(gptMessage);
    }

}
