package com.example.mugbackend.user.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class UserNotFoundException extends MulgaException {
	public UserNotFoundException() {
		super(UserErrorCode.USER_NOT_FOUND);
	}
}
