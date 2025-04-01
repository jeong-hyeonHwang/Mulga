package com.example.mugbackend.analysis.dto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.common.enumeration.CategoryEnum;

import com.example.mugbackend.user.dto.CustomUserDetails;
import lombok.Builder;

@Builder
public record AnalysisDetailDto(
	String id, // Format: userId_year_month
	Integer year,
	Integer month,
	Integer monthTotal,
	Map<String, Integer> category,
	Map<String, Integer> paymentMethod,
	Map<Integer, Integer> monthlyTrend,
	Map<Integer, Integer> lastMonthAccumulation,
	Map<Integer, Integer> thisMonthAccumulation
) {
	public static AnalysisDetailDto of(Analysis lastMonthAnalysis, Analysis thisMonthAnalysis) {
		return AnalysisDetailDto.builder()
			.id(thisMonthAnalysis.getId())
			.year(thisMonthAnalysis.getYear())
			.month(thisMonthAnalysis.getMonth())
					.monthTotal(thisMonthAnalysis.getMonthTotal())
					.category(getFormattedCategory(thisMonthAnalysis.getCategory()))
					.paymentMethod(getFormattedPaymentMethod(thisMonthAnalysis.getPaymentMethod()))
					.lastMonthAccumulation((calMonthAccumulation(lastMonthAnalysis.getDaily())))
					.thisMonthAccumulation((calMonthAccumulation(thisMonthAnalysis.getDaily())))
			.build();
	}

	private static Map<String, Integer> getFormattedCategory(Map<String, Integer> category) {
		if (category == null) {
			category = new HashMap<>();
		}

		for (CategoryEnum categoryName : CategoryEnum.values()) {
			category.putIfAbsent(categoryName.name(), 0);
		}

		Map<String, Integer> sortedCategory = category.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(
						entry -> entry.getKey(),
						entry -> entry.getValue(),
						(e1, e2) -> e1,
						LinkedHashMap::new
				));
		
		return sortedCategory;
	}

	private static Map<String, Integer> getFormattedPaymentMethod(Map<String, Integer> paymentMethod) {
		if (paymentMethod == null) {
			paymentMethod = new HashMap<>();
		}

		Map<String, Integer> sortedPaymentMethod = paymentMethod.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(
						entry -> entry.getKey(),
						entry -> entry.getValue(),
						(e1, e2) -> e1,
						LinkedHashMap::new
				));

		return sortedPaymentMethod;
	}

	private static Map<Integer, Integer> calMonthAccumulation(Map<Integer, Analysis.DailyAmount> daily) {
		if (daily == null) {
			daily = new HashMap<>();
		}

		Map<Integer, Integer> cumulativeExpense = new HashMap<>();
		int sum = 0;

		for(int day=1; day<=31; day++) {
			daily.putIfAbsent(day, Analysis.DailyAmount.builder().expense(0).income(0).isValid(false).build());
			sum += daily.get(day).getExpense();
			cumulativeExpense.put(day, sum);
		}

		return cumulativeExpense;
	}

}
