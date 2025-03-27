package com.example.mugbackend.transaction.dto;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTransactionDto {
    private int monthTotal;
    private int year;
    private int month;
    private Map<Integer, Analysis.DailyAmount> daily;
    private Map<Integer, List<Transaction>> transactions;
}
