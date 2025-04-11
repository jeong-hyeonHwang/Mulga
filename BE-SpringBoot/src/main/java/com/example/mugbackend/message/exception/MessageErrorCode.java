package com.example.mugbackend.message.exception;

import com.example.mugbackend.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageErrorCode implements ErrorCode {
	MESSAGE_FINANCENOTIDTO_CONVERSION_ERROR("MESSAGE_1000");

    private final String code;
}
