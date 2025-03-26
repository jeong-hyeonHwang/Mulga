package com.example.mugbackend.common.exception;

import lombok.Getter;

@Getter
public class MulgaException extends RuntimeException{
	private final ErrorCode errorCode;

	public MulgaException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}
