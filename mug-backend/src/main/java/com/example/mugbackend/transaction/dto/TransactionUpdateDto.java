package com.example.mugbackend.transaction.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.mugbackend.common.annotation.ValidCategory;
import com.example.mugbackend.common.annotation.ValidDateTimeFormat;
import com.example.mugbackend.transaction.domain.Transaction;

public record TransactionUpdateDto (
	@NotNull
	String id,
	@Min(2000) @Max(2100)
	Integer year,
	@Min(1) @Max(12)
	Integer month,
	@Min(1) @Max(31)
	Integer day,
	Boolean isCombined,
	@Size(max = 50)
	String title,
	Integer cost,
	@ValidCategory
	String category,
	String memo,
	String vendor,
	String bank,
	@ValidDateTimeFormat
	String time,
	@Size(max = 50)
	String paymentMethod
) {
	public Transaction toEntity() {
		Transaction transaction = new Transaction();

		transaction.setId(id);
		if (year != null) transaction.setYear(year);
		if (month != null) transaction.setMonth(month);
		if (day != null) transaction.setDay(day);
		if (title != null) transaction.setTitle(title);
		if (cost != null) transaction.setCost(cost);
		if (category != null) transaction.setCategory(category);
		if (memo != null) transaction.setMemo(memo);
		if (vendor != null) transaction.setVendor(vendor);
		if (bank != null) transaction.setBank(bank);
		if (time != null) transaction.setTime(LocalDateTime.parse(time));
		if (paymentMethod != null) transaction.setPaymentMethod(paymentMethod);
		return transaction;
	}
}

