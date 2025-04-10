package com.example.mugbackend.message.service;

import com.example.mugbackend.message.dto.FinanceNotiDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageHelperService {
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
        sb.append("위의 거래 내역 중 네이버페이에 충전된 금액(transfer 및 등 기타 내역)이 아닌, **실제 구매 내역**을 동일한 JSON 형태로 반환해 주세요.\n");
        sb.append("구매 내역은 itemName이 비어있지 않습니다.\n");
        sb.append("cost는 각 Json의 cost중 가장 작은 숫자로 넣어주세요.\n");
        sb.append("응답은 줄바꿈을 엄격히 해서 json 형식으로 주세요. json이라는 글자를 포함하지 마세요. 대괄호도 빼주세요. 백틱도 빼주세요\n");

        sb.append("### 프롬프트 끝");

        return sb.toString();
    }

    public static String normalizeGPTResponse(String input) {
        String result = input.replace("'", "\"");
        result = result.replaceAll("\"\\s+(?=[A-Za-z])", "\"");
        return result;
    }

}
