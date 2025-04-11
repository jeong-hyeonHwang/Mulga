package com.example.mugbackend.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceNotiDto {

    private String userId;
    private Integer year;
    private Integer month;
    private Integer day;
    private String itemName;
    private Integer cost;
    private String category;
    private String vendor;
    private LocalDateTime time;
    private String paymentMethod;
}