package com.example.mugbackend.user.exception;

import com.example.mugbackend.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND("USER_1000"),
	ACTIVE_USER_NOT_FOUND("USER_1001"),
	USER_ALREADY_EXISTS("USER_1002");
	private final String code;
}
