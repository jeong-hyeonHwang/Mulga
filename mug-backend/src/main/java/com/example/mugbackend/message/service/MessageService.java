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

    // 20초마다 실행
    @Scheduled(fixedRate = 20000)
    public void pollMessages() {

        List<String> messages = fetchMessages(10);
        if (!messages.isEmpty()) {

            for(String message : messages) {
                // 메시지 하나마다 gpt를 돌려야 한다.
                System.out.println("메시지 원본 : " + message);

                // 큐에서 메시지 못 가져왔으면 넘어감
                if(message.equals("큐에서 메시지를 가져오지 못했습니다.")) {
                    continue;
                }

//                // 카카오톡 메시지면 넘어감
//                if(message.contains("카카오톡")) {
//                    continue;
//                }

                String gptMessage = chatGPTService.getChatResponse(message);
                System.out.println("GPT API 응답 : " + gptMessage);

                // 금융알림이 아니면 넘어감
                if(gptMessage.equals("금융 알림이 아닙니다")) {
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


    // 60초마다 모든 알림 그룹을 검사해서 시작 시각으로부터 1분이 지난 그룹을 flush
    @Scheduled(fixedRate = 60000)
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


    private void flushGroup(String userId, FinanceNotiGroup group) {

        // financeNotiGroup이 어떤 금융 알림인지 판단하는 로직
        /*
        1. appName에 '카드' 또는 'card'가 포함된 경우(오프라인 카드 결제) -> 카드사 알림으로 Transaction 처리
        2. title이 '네이버페이' 또는 '네이버 페이'인 경우 -> paymentMethod가 '네이버페이' 인 알림으로 Transaction 처리
           ->
         */
        FinanceNotiDto cardNoti = group.getNotifications().stream()
                .filter(dto -> dto.getPaymentMethod() != null && dto.getPaymentMethod().contains("카드"))
                .findFirst()
                .orElse(null);

        if (cardNoti != null) {
            // cardNoti를 Transaction 엔티티로 변환
            Transaction newTransaction = convertToTransaction(cardNoti);
            // CustomUserDetails 만들기
            User user = userService.findUserById(userId);
            CustomUserDetails userDetails = CustomUserDetails.of(user);

            // db에 반영
            analysisService.addTransactionToAnalysis(userDetails, newTransaction);
            transactionRepository.save(newTransaction);

            System.out.println("[" + userId + "] 그룹 flush: 알림 저장");
        } else {
            // 2. 카드 알림이 아닌 경우(네이버페이 머니 충전결제라고 가정..) gpt한테 요청
            String message = makeNaverPayPrompt(group.getNotifications());
            String gptMessage = chatGPTService.getTheBestNoti(message);
            System.out.println("네이버페이의 GPT API 응답 : " + gptMessage);

            FinanceNotiDto naverNoti = convertToFinanceNotiDto(gptMessage);
            Transaction newTransaction = convertToTransaction(naverNoti);
            // CustomUserDetails 만들기
            User user = userService.findUserById(userId);
            CustomUserDetails userDetails = CustomUserDetails.of(user);

            // db에 반영
            analysisService.addTransactionToAnalysis(userDetails, newTransaction);
            transactionRepository.save(newTransaction);

            System.out.println("[" + userId + "] 그룹 flush: 알림 저장");

//            System.out.println("[" + userId + "] 그룹 flush: 알림 없음");
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

    public static String makeNaverPayPrompt(List<FinanceNotiDto> notiList) {
        StringBuilder sb = new StringBuilder();

        // 프롬프트 시작 부분 작성
        sb.append("### 프롬프트 시작\n");
        sb.append("다음은 ").append(notiList.size()).append("개의 거래 내역을 나타내는 JSON DTO입니다:\n\n");

        // 각 DTO를 JSON 문자열 형태로 추가 (번호도 붙임)
        for (int i = 0; i < notiList.size(); i++) {
            FinanceNotiDto dto = notiList.get(i);
            sb.append(i + 1).append(".\n");
            sb.append("{\n");
            sb.append("  \"userId\": \"").append(dto.getUserId()).append("\",\n");
            sb.append("  \"year\": ").append(dto.getYear()).append(",\n");
            sb.append("  \"month\": ").append(dto.getMonth()).append(",\n");
            sb.append("  \"day\": ").append(dto.getDay()).append(",\n");
            sb.append("  \"itemName\": \"").append(dto.getItemName() == null ? "" : dto.getItemName()).append("\",\n");
            sb.append("  \"cost\": ").append(dto.getCost()).append(",\n");
            sb.append("  \"category\": \"").append(dto.getCategory()).append("\",\n");
            sb.append("  \"vendor\": \"").append(dto.getVendor()).append("\",\n");
            sb.append("  \"time\": \"").append(dto.getTime()).append("\",\n");
            sb.append("  \"paymentMethod\": \"").append(dto.getPaymentMethod()).append("\"\n");
            sb.append("}\n\n");
        }

        // 프롬프트 마지막에 요청 사항 추가
        sb.append("위의 거래 내역 중 네이버페이에 충전된 금액(transfer 및 등 기타 내역)이 아닌, **실제 구매 내역**만을 반환해 주세요.\n");
        sb.append("구매 내역은 itemName이 비어있지 않은 내역입니다.\n");
        sb.append("응답은 줄바꿈을 엄격히 해서 json 형식으로 주세요. json이라는 글자를 포함하지 마세요. 대괄호도 빼주세요. 백틱도 빼주세요\n");

        sb.append("### 프롬프트 끝");

        return sb.toString();
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
