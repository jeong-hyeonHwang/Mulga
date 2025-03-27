package com.example.mugbackend.transaction.dto;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;

import java.util.List;
import java.util.Map;

public class MonthlyTransactionDto {
    private int monthTotal;
    private int year;
    private int month;
    private Map<Integer, Analysis.DailyAmount> daily;
    private Map<Integer, List<Transaction>> transactions;
}
