package com.example.mugbackend.analysis.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.common.enumeration.CategoryEnum;

import lombok.Builder;

@Builder
public record AnalysisDetailDto(
		String id, // Format: userId_year_month
		Integer year,
		Integer month,
		Integer monthTotal,
		Map<String, Integer> category,
		Map<String, Integer> paymentMethod,
		Map<Integer, Analysis.DailyAmount> daily
) {
	public static AnalysisDetailDto of(Analysis analysis) {
		return AnalysisDetailDto.builder()
				.id(analysis.getId())
				.year(analysis.getYear())
				.month(analysis.getMonth())
				.monthTotal(analysis.getMonthTotal())
				.category(getFormattedCategory(analysis.getCategory()))
				.paymentMethod(getFormattedPaymentMethod(analysis.getPaymentMethod()))
				.daily(getFormattedDaily(analysis.getDaily()))
				.build();
	}

	private static Map<String, Integer> getFormattedCategory(Map<String, Integer> category) {
		if (category == null) {
			category = new HashMap<>();
		}

		for (CategoryEnum categoryName : CategoryEnum.values()) {
		category.putIfAbsent(categoryName.name(), 0);
	}

		return category;
}

private static Map<String, Integer> getFormattedPaymentMethod(Map<String, Integer> paymentMethod) {
		if (paymentMethod == null) {
			paymentMethod = new HashMap<>();
		}
		return paymentMethod;
	}

	private static Map<Integer, Analysis.DailyAmount> getFormattedDaily(Map<Integer, Analysis.DailyAmount> daily) {
		if (daily == null) {
			daily = new HashMap<>();
		}
		for(int day=1; day<=31; day++) {
			daily.putIfAbsent(day, Analysis.DailyAmount.builder().expense(0).income(0).isValid(false).build());
		}
		return daily;
	}
}
