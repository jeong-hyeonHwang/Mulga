package com.example.mugbackend.message.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final String QUEUE_NAME = "q.mulga";

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<String> fetchMessages(int count) {
        System.out.println("서비스: fetchMessages 메서드 진입");


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

                System.out.println("서비스: message from rabbitMQ: " + message);


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

        // 큐에서 메시지를 하나도 가져오지 못했을 경우, 대체 메시지를 추가
        if (messages.isEmpty()) {
            messages.add("요청한 메시지가 충분하지 않습니다.");
        }

        return messages;
    }
}
