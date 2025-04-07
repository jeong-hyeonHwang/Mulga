package com.example.mugbackend.message.service;

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

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // 10초마다 실행
    @Scheduled(fixedRate = 10000)
    public void pollMessages() {

        // TODO: 출력문 삭제
        System.out.println("pollingMessage메서드 실행");

        List<String> messages = fetchMessages(2);
        if (!messages.isEmpty()) {

            // TODO: 출력문 삭제
            System.out.println("가져온 메시지 개수: " + messages.size());

            for(String message : messages) {
                System.out.println(message);
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
