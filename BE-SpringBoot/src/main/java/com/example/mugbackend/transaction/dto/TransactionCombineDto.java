package com.example.mugbackend.transaction.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.example.mugbackend.common.annotation.ValidMainTransactionId;

@ValidMainTransactionId
public record TransactionCombineDto (
	@NotBlank
	String mainTransactionId,
	@NotNull @NotEmpty
	List<String> combiningTransactionIds
){
}
