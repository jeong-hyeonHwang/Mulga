package com.example.mugbackend.user.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class ActiveUserNotFoundException extends MulgaException {
	public ActiveUserNotFoundException() {
		super(UserErrorCode.ACTIVE_USER_NOT_FOUND);
	}
}
