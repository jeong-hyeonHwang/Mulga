package com.example.mugbackend.user.dto;

import com.example.mugbackend.transaction.dto.TransactionDetailDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainDto {
    private int monthTotal;
    private int remainingBudget;
    private TransactionDetailDto lastTransaction;
}
