package com.example.mugbackend.message.controller;


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

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/rabbit-message")
    public ResponseEntity<String> rabbitMessage(@RequestBody MessageRequestDto requestDto) {

        System.out.println("컨트롤러: 진입");
        // 서비스에서 큐에서 메시지를 가져옵니다.
        List<String> messages = messageService.fetchMessages(requestDto.getCount());

        System.out.println("컨트롤러: service 끝남." + messages.get(0));



        // 만약 큐에 메시지가 없다면 "큐에 메시지 없음"이라는 문자열을 응답으로 보냅니다.
        if (messages.isEmpty()) {
            return ResponseEntity.ok("큐에 메시지 없음");
        }

        // 여기서는 첫 번째 메시지만 응답으로 반환합니다.
        return ResponseEntity.ok(messages.get(0));
    }

}
