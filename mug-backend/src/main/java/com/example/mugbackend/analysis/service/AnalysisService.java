package com.example.mugbackend.analysis.service;

import com.example.mugbackend.analysis.dto.AnalysisFullDetailDto;
import com.example.mugbackend.analysis.dto.MonthlyTrendDto;
import com.example.mugbackend.common.enumeration.CategoryEnum;
import org.springframework.stereotype.Service;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.dto.AnalysisDetailDto;
import com.example.mugbackend.analysis.exception.AnalysisNotFoundException;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {
	private final AnalysisRepository analysisRepository;

	public AnalysisFullDetailDto getAnalysisFullDetail(CustomUserDetails userDetails, Integer year, Integer month) {

		// 이번 달의 analysis가 없으면 예외 처리, 과거 달의 analysis가 없으면 새로 만든다.
		String thisMonthId = getId(userDetails.id(), year, month);
		Analysis thisMonthAnalysis = analysisRepository.findById(thisMonthId)
				.orElseThrow(AnalysisNotFoundException::new);

		int lastYear = month==1 ? year-1 : year;
		int lastMonth = month==1 ? 12 : month-1;
		Analysis lastMonthAnalysis = getAnalysis(userDetails.id(), lastYear, lastMonth);

		return AnalysisFullDetailDto.builder()
				.id(thisMonthAnalysis.getId())
				.year(thisMonthAnalysis.getYear())
				.month(thisMonthAnalysis.getMonth())
				.monthTotal(thisMonthAnalysis.getMonthTotal())
				.category(getSortedCategory(thisMonthAnalysis.getCategory()))
				.paymentMethod(getSortedPaymentMethod(thisMonthAnalysis.getPaymentMethod()))
				.monthlyTrend(getMonthlyTrend(thisMonthAnalysis, userDetails.id(), year, month))
				.lastMonthAccumulation((calMonthAccumulation(lastMonthAnalysis.getDaily())))
				.thisMonthAccumulation((calMonthAccumulation(thisMonthAnalysis.getDaily())))
				.build();
	}

	public List<MonthlyTrendDto> getMonthlyTrend(Analysis analysis, String userId, int year, int month) {

		LinkedList<MonthlyTrendDto> monthlyTrend = new LinkedList<>();

		for(int i=0; i<6; i++) {
			monthlyTrend.addFirst(new MonthlyTrendDto(year, month, analysis.getMonthTotal()));

			year = month==1 ? year-1 : year;
			month = month==1 ? 12 : month-1;
			analysis = getAnalysis(userId, year, month);
		}
		return monthlyTrend;
	}

	public String getId(String userId, int year, int month) {
		return String.format("%s_%d_%d", userId, year, month);
	}

	public Map<String, Integer> getSortedCategory(Map<String, Integer> category) {
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

	public Map<String, Integer> getSortedPaymentMethod(Map<String, Integer> paymentMethod) {
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




	private Analysis getAnalysis(String userId, Integer year, Integer month) {

		String id = getId(userId, year, month);

		return analysisRepository.findById(id)
				.orElseGet(() -> Analysis.builder()
						.id(id)
						.year(year)
						.month(month)
						.monthTotal(0)
						.category(new HashMap<>())
						.paymentMethod(new HashMap<>())
						.daily(new HashMap<>())
						.build());
	}

	public Map<Integer, Integer> calMonthAccumulation(Map<Integer, Analysis.DailyAmount> daily) {
		if (daily == null) {
			daily = new HashMap<>();
		}

		Map<Integer, Integer> cumulativeExpense = new LinkedHashMap<>();
		int sum = 0;

		for(int day=1; day<=31; day++) {
			daily.putIfAbsent(day, Analysis.DailyAmount.builder().expense(0).income(0).isValid(false).build());
			sum += daily.get(day).getExpense();
			cumulativeExpense.put(day, sum);
		}
		return cumulativeExpense;
	}
}
