package com.example.mugbackend.user.exception;

import com.example.mugbackend.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND("USER_1000");
	private final String code;
}
