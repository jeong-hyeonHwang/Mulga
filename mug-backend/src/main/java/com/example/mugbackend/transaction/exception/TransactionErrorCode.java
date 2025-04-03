package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionErrorCode implements ErrorCode {
	TRANSACTION_NOT_FOUND("TRANSACTION_1000"),
	TRANSACTION_ACCESS_DENIED("TRANSACTION_1001"),
    TRANSACTION_NO_HISTORY("TRANSACTION_1002"),
	TRANSACTION_COMBINE_CONFLICT("TRANSACTION_1003");

    private final String code;
}
