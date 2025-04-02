package com.example.mugbackend.transaction.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.mugbackend.transaction.domain.Transaction;

import lombok.Builder;

@Builder
public record TransactionDetailDto (
	String id,
	String userId,
	Integer year,
	Integer month,
	Integer day,
	Boolean isCombined,
	String title,
	Integer cost,
	String category,
	String memo,
	String vendor,
	String bank,
	LocalDateTime time,
	String paymentMethod,
	List<Transaction> group
){
	static public TransactionDetailDto of(Transaction transaction) {
		return TransactionDetailDto.builder()
			.id(transaction.getId())
			.userId(Optional.ofNullable(transaction.getUserId()).orElse(""))
			.year(Optional.ofNullable(transaction.getYear()).orElse(0))
			.month(Optional.ofNullable(transaction.getMonth()).orElse(0))
			.day(Optional.ofNullable(transaction.getDay()).orElse(0))
			.isCombined(Optional.ofNullable(transaction.getIsCombined()).orElse(false))
			.title(Optional.ofNullable(transaction.getTitle()).orElse(""))
			.cost(Optional.ofNullable(transaction.getCost()).orElse(0))
			.category(Optional.ofNullable(transaction.getCategory()).orElse(""))
			.memo(Optional.ofNullable(transaction.getMemo()).orElse(""))
			.vendor(Optional.ofNullable(transaction.getVendor()).orElse(""))
			.bank(Optional.ofNullable(transaction.getBank()).orElse(""))
			.time(transaction.getTime())
			.paymentMethod(Optional.ofNullable(transaction.getPaymentMethod()).orElse(""))
			.group(Optional.ofNullable(transaction.getGroup()).orElse(new ArrayList<>()))
			.build();
	}
}
