package com.example.mugbackend.transaction.dto;

import java.util.List;
import java.util.Map;

import com.example.mugbackend.analysis.domain.Analysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTransactionDto {
    private int monthTotal;
    private int year;
    private int month;
    private Map<Integer, Analysis.DailyAmount> daily;
    private Map<Integer, List<TransactionDetailDto>> transactions;
}
