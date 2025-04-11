package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class TransactionAccessDeniedException extends MulgaException {
	public TransactionAccessDeniedException() {
		super(TransactionErrorCode.TRANSACTION_ACCESS_DENIED);
	}
}
