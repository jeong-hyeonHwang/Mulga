package com.example.mugbackend.message.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class MessageConversionException extends MulgaException {
	public MessageConversionException() {
		super(MessageErrorCode.MESSAGE_FINANCENOTIDTO_CONVERSION_ERROR);
	}
}
