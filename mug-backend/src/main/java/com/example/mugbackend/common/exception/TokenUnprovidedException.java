package com.example.mugbackend.common.exception;

public class TokenUnprovidedException extends MulgaException{
	public TokenUnprovidedException() {
		super(CommonErrorCode.TOKEN_UNPROVIDED_EXCEPTION);
	}
}
