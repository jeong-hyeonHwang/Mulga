package com.example.mugbackend.transaction.dto;

import java.time.LocalDateTime;
import java.util.List;

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
			.userId(transaction.getUserId())
			.year(transaction.getYear())
			.month(transaction.getMonth())
			.day(transaction.getDay())
			.isCombined(transaction.getIsCombined())
			.title(transaction.getTitle())
			.cost(transaction.getCost())
			.category(transaction.getCategory())
			.memo(transaction.getMemo())
			.vendor(transaction.getVendor())
			.bank(transaction.getBank())
			.time(transaction.getTime())
			.paymentMethod(transaction.getPaymentMethod())
			.group(transaction.getGroup())
			.build();
	}
}
