package com.example.mugbackend.common.exception;

public class TokenInvalidException extends MulgaException{
	public TokenInvalidException() {
		super(CommonErrorCode.TOKEN_INVALID_EXCEPTION);
	}
}
