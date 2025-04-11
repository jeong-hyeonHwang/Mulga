package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class TransactionNotFoundException extends MulgaException {
	public TransactionNotFoundException() {
		super(TransactionErrorCode.TRANSACTION_NOT_FOUND);
	}
}
