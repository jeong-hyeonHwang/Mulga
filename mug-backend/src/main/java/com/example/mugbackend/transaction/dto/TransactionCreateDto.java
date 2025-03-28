package com.example.mugbackend.transaction.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.mugbackend.common.annotation.ValidCategory;
import com.example.mugbackend.common.annotation.ValidDateTimeFormat;
import com.example.mugbackend.transaction.domain.Transaction;

public record TransactionCreateDto (
	@NotNull @Min(2000) @Max(2100)
	Integer year,
	@NotNull @Min(1) @Max(12)
	Integer month,
	@NotNull @Min(1) @Max(31)
	Integer day,
	Boolean isCombined,
	@Size(max = 50)
	String title,
	@NotNull
	Integer cost,
	@ValidCategory
	String category,
	String memo,
	String vendor,
	String bank,
	@ValidDateTimeFormat
	String time,
	@NotBlank @Size(max = 50)
	String paymentMethod
) {
	public Transaction toEntity() {
		return Transaction.builder()
			.year(year)
			.month(month)
			.day(day)
			.isCombined(false)
			.title(Optional.ofNullable(title).orElse(""))
			.cost(cost)
			.category(category)
			.memo(Optional.ofNullable(memo).orElse(""))
			.vendor(Optional.ofNullable(vendor).orElse(""))
			.bank(Optional.ofNullable(bank).orElse(""))
			.time(LocalDateTime.parse(time))
			.paymentMethod(paymentMethod)
			.group(new ArrayList<>())
			.build();
	}
}
