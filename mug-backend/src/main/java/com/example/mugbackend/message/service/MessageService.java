package com.example.mugbackend.message.service;

import com.example.mugbackend.gpt.dto.PromptRequest;
import com.example.mugbackend.gpt.service.ChatGPTService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final String QUEUE_NAME = "q.mulga";
    private final ChatGPTService chatGPTService;

    public MessageService(RabbitTemplate rabbitTemplate, ChatGPTService chatGPTService) {
        this.rabbitTemplate = rabbitTemplate;
        this.chatGPTService = chatGPTService;
    }

    // 20초마다 실행
    @Scheduled(fixedRate = 10000)
    public void pollMessages() {

        List<String> messages = fetchMessages(5);
        if (!messages.isEmpty()) {

            for(String message : messages) {
                // 메시지 하나마다 gpt를 돌려야 한다.

                System.out.println("메시지 원본 : " + message);

                String gptMessage = chatGPTService.getChatResponse(message);
                System.out.println("GPT API 응답 : " + gptMessage);

            }

        }
    }

    public List<String> fetchMessages(int count) {

        List<String> messages = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {

                Object obj = rabbitTemplate.receiveAndConvert(QUEUE_NAME);
                String message;
                if (obj == null) {
                    break;
                } else if (obj instanceof byte[]) {
                    message = new String((byte[]) obj, java.nio.charset.StandardCharsets.UTF_8);
                } else if (obj instanceof String) {
                    message = (String) obj;
                } else {
                    message = obj.toString();
                }


                if (message == null) {
                    break;
                }
                messages.add(message);
            } catch(Exception e) {

                System.err.println("메시지를 가져오는 중 예외 발생: " + e.getMessage());
                e.printStackTrace();
                break;
            }

        }

        if (messages.isEmpty()) {
            messages.add("큐에서 메시지를 가져오지 못했습니다.");
        }

        return messages;
    }
}
