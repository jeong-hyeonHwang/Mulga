package com.example.mugbackend.message.controller;


import com.example.mugbackend.gpt.dto.PromptRequest;
import com.example.mugbackend.gpt.service.ChatGPTService;
import com.example.mugbackend.message.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final ChatGPTService chatGPTService;
    public MessageController(MessageService messageService, ChatGPTService chatGPTService) {
        this.messageService = messageService;
        this.chatGPTService = chatGPTService;
    }

    // 테스트 위해 큐에서 1개씩 꺼내는 용도
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

    // convertToFinanceNotiDto 메서드 테스트 용도
    @GetMapping("/test")
    public void test() {
        String json = "{\n" +
                "  \"userIdadsf\": \"aAeovBlrrEWTRTSCSL1p6scYI2q2\",\n" +
                "  \"year\": 2025,\n" +
                "  \"month\": 4,\n" +
                "  \"day\": 9,\n" +
                "  \"itemName\": \"\",\n" +
                "  \"cost\": -600,\n" +
                "  \"category\": \"etc\",\n" +
                "  \"vendor\": \"ＧＳ２５지에스사\",\n" +
                "  \"time\": \"2025-04-09T01:17:21Z\",\n" +
                "  \"paymentMethod\": \"우리카드\"\n" +
                "}";
        messageService.convertToFinanceNotiDto(json);
        System.out.println("test");
    }


}
