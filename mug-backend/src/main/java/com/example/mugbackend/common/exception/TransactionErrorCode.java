package com.example.mugbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionErrorCode implements ErrorCode {
    TRANSACTION_NOT_FOUND("TRANSACTION_1000");

    private final String code;
}
