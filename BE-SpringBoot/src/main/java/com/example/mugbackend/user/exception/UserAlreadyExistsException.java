package com.example.mugbackend.user.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class UserAlreadyExistsException extends MulgaException {
	public UserAlreadyExistsException() {
		super(UserErrorCode.USER_ALREADY_EXISTS);
	}
}
