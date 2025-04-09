package com.example.mugbackend.message.service;

import com.example.mugbackend.gpt.dto.PromptRequest;
import com.example.mugbackend.gpt.service.ChatGPTService;
import com.example.mugbackend.message.dto.FinanceNotiDto;
import com.example.mugbackend.message.exception.MessageConversionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    // 5초마다 실행
    @Scheduled(fixedRate = 5000)
    public void pollMessages() {

        List<String> messages = fetchMessages(20);
        if (!messages.isEmpty()) {

            for(String message : messages) {
                // 메시지 하나마다 gpt를 돌려야 한다.
                System.out.println("메시지 원본 : " + message);

                // 카카오톡 메시지면 넘어감
                if(message.contains("카카오톡")) {
                    continue;
                }

                String gptMessage = chatGPTService.getChatResponse(message);
                System.out.println("GPT API 응답 : " + gptMessage);

                // 금융알림이 아니면 넘어감
                if(gptMessage.equals("금융 알림이 아닙니다.")) {
                    continue;
                }
                // 금융 알림이면 DTO로 변환
                FinanceNotiDto financeNotiDto = convertToFinanceNotiDto(gptMessage);

                // 사용자별 시간 제한 1분인 그룹 만들기
                // 최종적으로 남은 알림 1개만 Transaction 엔티티로 만들어서 저장~

                /*
                createTransactionFromPush 메서드를 만들어서
                CustomUserDetails, Transactions를 넘겨줘서 아래 두 줄 실행
        analysisService.addTransactionToAnalysis(userDetails, transaction);
        transactionRepository.save(transaction);

                 */


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

    public FinanceNotiDto convertToFinanceNotiDto(String message) {
        FinanceNotiDto financeNotiDto = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            financeNotiDto = objectMapper.readValue(message, FinanceNotiDto.class);

            System.out.println("변환된 FinanceNotiDto: " + financeNotiDto);
            return financeNotiDto;

        } catch (Exception e) {
            throw new MessageConversionException();
        }
    }
}
