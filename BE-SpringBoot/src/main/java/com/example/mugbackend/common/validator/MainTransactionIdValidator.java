package com.example.mugbackend.common.validator;

import java.util.HashSet;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.mugbackend.common.annotation.ValidMainTransactionId;
import com.example.mugbackend.transaction.dto.TransactionCombineDto;

public class MainTransactionIdValidator implements ConstraintValidator<ValidMainTransactionId, TransactionCombineDto> {
	@Override
	public boolean isValid(TransactionCombineDto dto, ConstraintValidatorContext context) {
		return !new HashSet<>(dto.combiningTransactionIds()).contains(dto.mainTransactionId());
	}
}
