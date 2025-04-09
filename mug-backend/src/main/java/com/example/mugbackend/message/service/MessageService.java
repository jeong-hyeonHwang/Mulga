package com.example.mugbackend.message.service;

import com.example.mugbackend.analysis.service.AnalysisService;
import com.example.mugbackend.gpt.service.ChatGPTService;
import com.example.mugbackend.message.dto.FinanceNotiDto;
import com.example.mugbackend.message.exception.MessageConversionException;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.repository.TransactionRepository;
import com.example.mugbackend.user.domain.User;
import com.example.mugbackend.user.dto.CustomUserDetails;
import com.example.mugbackend.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.Getter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;
    private final String QUEUE_NAME = "q.mulga";
    private final ChatGPTService chatGPTService;
    private final UserService userService;
    private final AnalysisService analysisService;
    private final TransactionRepository transactionRepository;

    // 사용자별 금융 알림 그룹을 저장 (추후 동시성 고려를 위해 ConcurrentHashMap 사용)
    private final Map<String, FinanceNotiGroup> userNotiGroups = new ConcurrentHashMap<>();

    public MessageService(RabbitTemplate rabbitTemplate, ChatGPTService chatGPTService, UserService userService, AnalysisService analysisService, TransactionRepository transactionRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.chatGPTService = chatGPTService;
        this.userService = userService;
        this.analysisService = analysisService;
        this.transactionRepository = transactionRepository;
    }

    // 60초마다 실행
    @Scheduled(fixedRate = 60000)
    public void pollMessages() {

        List<String> messages = fetchMessages(5);
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
                // 최종적으로 남은 알림 1개만 Transaction 엔티티로 만들어서 저장

                // userId를 기준으로 그룹에 추가 (GPT 응답의 time 필드를 기준으로 1분동안 살아있음)
                String userId = financeNotiDto.getUserId();
                LocalDateTime notiTime = financeNotiDto.getTime(); // 해당 알림의 시간

                // 해당 사용자의 그룹을 일단 가져옴. userId에 해당하는 key가 없으면 null
                FinanceNotiGroup group = userNotiGroups.get(userId);
// userNotiGroups가 이것임
// private final Map<String, FinanceNotiGroup> userNotiGroups = new ConcurrentHashMap<>();
                if (group == null) {
                    // 없으면 만들고 userNotiGroups에 추가
                    group = new FinanceNotiGroup(notiTime);
                    group.getNotifications().add(financeNotiDto);
                    userNotiGroups.put(userId, group);

                    System.out.println("[" + userId + "] 새 그룹 생성, 시작시간: " + notiTime);
                } else {
                    // 해당 사용자의 그룹이 있으면 시작시간과 현재 알림 시간 비교
                    long secondsElapsed = Duration.between(group.getStartTime(), notiTime).getSeconds();
                    if (secondsElapsed <= 60) {
                        // 1분 이내이면 그룹의 notifications에 추가
                        group.getNotifications().add(financeNotiDto);

                        System.out.println("[" + userId + "] 그룹에 알림 추가 (" + secondsElapsed + "초 경과), 알림시간: " + notiTime);
                    } else {
                        // 1분 초과이면 기존 그룹 flush, 기존 그룹의 금융 알림 처리 후 새 그룹 생성
                        flushGroup(userId, group);
                        FinanceNotiGroup newGroup = new FinanceNotiGroup(notiTime);
                        newGroup.getNotifications().add(financeNotiDto);
                        userNotiGroups.put(userId, newGroup);

                        System.out.println("[" + userId + "] 그룹 flush 후 새 그룹 생성, 첫 알림시간: " + notiTime);
                    }
                }




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


    // 10초마다 모든 알림 그룹을 검사해서 시작 시각으로부터 1분이 지난 그룹을 flush
    @Scheduled(fixedRate = 10000)
    public void flushStaleGroups() {
        LocalDateTime now = LocalDateTime.now();
        Iterator<Map.Entry<String, FinanceNotiGroup>> iterator = userNotiGroups.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FinanceNotiGroup> entry = iterator.next();
            String userId = entry.getKey();
            FinanceNotiGroup group = entry.getValue();
            long secondsElapsed = Duration.between(group.getStartTime(), now).getSeconds();
            if (secondsElapsed > 60) {
                flushGroup(userId, group);
                iterator.remove();
            }
        }
    }


    // 그룹 내 알림 중 "카드"라는 문자열이 포함된 알림만 DB에 저장
    private void flushGroup(String userId, FinanceNotiGroup group) {
        FinanceNotiDto cardNoti = group.getNotifications().stream()
                .filter(dto -> dto.getPaymentMethod() != null && dto.getPaymentMethod().contains("카드"))
                .findFirst()
                .orElse(null);
        if(cardNoti == null) {
            System.out.println("[" + userId + "] 그룹 flush: 카드 알림 없음");
            return;
        }

        if (cardNoti != null) {
            // cardNoti를 Transaction 엔티티로 변환
            Transaction newTransaction = convertToTransaction(cardNoti);
            // CustomUserDetails 만들기
            User user = userService.findUserById(userId);
            CustomUserDetails userDetails = CustomUserDetails.of(user);

            // db에 반영
            analysisService.addTransactionToAnalysis(userDetails, newTransaction);
            transactionRepository.save(newTransaction);

            System.out.println("[" + userId + "] 그룹 flush: 카드 알림 저장");
        } else {
            System.out.println("[" + userId + "] 그룹 flush: 카드 알림 없음");
        }
    }

    public Transaction convertToTransaction(FinanceNotiDto dto) {
        return Transaction.builder()
                .userId(dto.getUserId())
                .year(dto.getYear())
                .month(dto.getMonth())
                .day(dto.getDay())
                .isCombined(false)
                .title(dto.getItemName())
                .cost(dto.getCost())
                .category(dto.getCategory())
                .memo("")
                .vendor(dto.getVendor())
                .time(dto.getTime())
                .paymentMethod(dto.getPaymentMethod())
                .group(new ArrayList<>())
                .build();
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

    // 한 사용자의 특정 금융 알림 그룹을 관리하기 위한 내부 클래스.
    @Data
    private static class FinanceNotiGroup {
        private final LocalDateTime startTime;
        private final List<FinanceNotiDto> notifications = new ArrayList<>();

        public FinanceNotiGroup(LocalDateTime startTime) {
            this.startTime = startTime;
        }
    }

}
