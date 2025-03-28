package com.example.mugbackend.transaction.exception;

import com.example.mugbackend.transaction.exception.TransactionErrorCode;
import com.example.mugbackend.common.exception.MulgaException;

public class TransactionNoHistorzyException extends MulgaException {
	public TransactionNoHistoryException() {
		super(TransactionErrorCode.TRANSACTION_NO_HISTORY);
	}
}
