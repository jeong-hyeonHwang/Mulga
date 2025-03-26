package com.example.mugbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	VALIDATION_FAILED("COMMON_1000"),
	JSON_PARSING_FAILED("COMMON_1001"),
	INVALID_ENUM_EXCEPTION("COMMON_1002"),
	INTERNAL_SERVER_ERROR("COMMON_1003");

	private final String code;
}
