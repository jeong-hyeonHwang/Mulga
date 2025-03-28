package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionErrorCode implements ErrorCode {
    TRANSACTION_NO_HISTORY("TRANSACTION_1002");

    private final String code;
}
