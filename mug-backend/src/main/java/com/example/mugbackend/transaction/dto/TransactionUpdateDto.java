package com.example.mugbackend.transaction.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.data.mongodb.core.query.Update;

import com.example.mugbackend.common.annotation.ValidCategory;
import com.example.mugbackend.common.annotation.ValidDateTimeFormat;

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
	public Update toUpdate() {
		Update update = new Update();

		if (year != null) update.set("year", year);
		if (month != null) update.set("month", month);
		if (day != null) update.set("day", day);
		if (title != null) update.set("title", title);
		if (cost != null) update.set("cost", cost);
		if (category != null) update.set("category", category);
		if (memo != null) update.set("memo", memo);
		if (vendor != null) update.set("vendor", vendor);
		if (bank != null) update.set("bank", bank);
		if (time != null) update.set("time", time);
		if (paymentMethod != null) update.set("paymentMethod", paymentMethod);

		return update;
	}
}

