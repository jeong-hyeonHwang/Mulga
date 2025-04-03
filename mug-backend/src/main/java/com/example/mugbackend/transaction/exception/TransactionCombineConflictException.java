package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class TransactionCombineConflictException extends MulgaException {
	public TransactionCombineConflictException() {
		super(TransactionErrorCode.TRANSACTION_COMBINE_CONFLICT);
	}
}
