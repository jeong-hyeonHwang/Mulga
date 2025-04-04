package com.example.mugbackend.analysis.service;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.dto.AnalysisDetailDto;
import com.example.mugbackend.analysis.dto.AnalysisFullDetailDto;
import com.example.mugbackend.analysis.dto.MonthlyTrendDto;
import com.example.mugbackend.analysis.exception.AnalysisNotFoundException;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.common.enumeration.CategoryEnum;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {
	private final AnalysisRepository analysisRepository;

	public AnalysisDetailDto getAnalysisDetail(CustomUserDetails userDetails, Integer year, Integer month) {
		String id = String.format("%s_%d_%d", userDetails.id(), year, month);

		System.out.println(id);
		Analysis analysis = analysisRepository.findById(id)
			.orElseThrow(AnalysisNotFoundException::new);
		return AnalysisDetailDto.of(analysis);
	}

    public AnalysisFullDetailDto getAnalysisFullDetail(CustomUserDetails userDetails, Integer year, Integer month) {
		Analysis thisMonthAnalysis = getAnalysis(userDetails, year, month);

		int lastYear = month == 1 ? year - 1 : year;
		int lastMonth = month == 1 ? 12 : month - 1;
		Analysis lastMonthAnalysis = getAnalysis(userDetails, lastYear, lastMonth);

		return AnalysisFullDetailDto.builder()
				.id(thisMonthAnalysis.getId())
				.year(thisMonthAnalysis.getYear())
				.month(thisMonthAnalysis.getMonth())
				.monthTotal(thisMonthAnalysis.getMonthTotal())
				.category(getSortedCategory(thisMonthAnalysis.getCategory()))
				.paymentMethod(getSortedPaymentMethod(thisMonthAnalysis.getPaymentMethod()))
				.monthlyTrend(getMonthlyTrend(thisMonthAnalysis, userDetails, year, month))
				.lastMonthAccumulation((calMonthAccumulation(lastMonthAnalysis.getDaily())))
				.thisMonthAccumulation((calMonthAccumulation(thisMonthAnalysis.getDaily())))
				.build();
	}

	public List<MonthlyTrendDto> getMonthlyTrend(Analysis analysis, CustomUserDetails userDetails, int year, int month) {

        LinkedList<MonthlyTrendDto> monthlyTrend = new LinkedList<>();

        for(int i=0; i<6; i++) {
            monthlyTrend.addFirst(new MonthlyTrendDto(year, month, analysis.getMonthTotal()));

            year = month==1 ? year-1 : year;
            month = month==1 ? 12 : month-1;
            analysis = getAnalysis(userDetails, year, month);
        }
        return monthlyTrend;
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


	@Transactional
	public AnalysisDetailDto addTransactionToAnalysis(CustomUserDetails userDetails, Transaction transaction) {
		Analysis analysis = getAnalysis(userDetails, transaction.getYear(), transaction.getMonth());

		applyChangeToAnalysis(analysis, transaction, true);

		analysisRepository.updateAnalysis(analysis, transaction);

		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto removeTransactionFromAnalysis(CustomUserDetails userDetails, Transaction transaction) {
		Analysis analysis = getAnalysis(userDetails, transaction.getYear(), transaction.getMonth());

		applyChangeToAnalysis(analysis, transaction, false);

		analysisRepository.updateAnalysis(analysis, transaction);

		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto alterTransactionToAnalysis(CustomUserDetails userDetails, Transaction beforeTransaction, Transaction currentTransaction) {
		if(!beforeTransaction.getYear().equals(currentTransaction.getYear()) || !(beforeTransaction.getMonth().equals(currentTransaction.getMonth()))) {
			Analysis beforeAnalysis = getAnalysis(userDetails, beforeTransaction.getYear(), beforeTransaction.getMonth());
			Analysis currentAnalysis = getAnalysis(userDetails, currentTransaction.getYear(), currentTransaction.getMonth());

			applyChangeToAnalysis(beforeAnalysis, beforeTransaction, false);
			applyChangeToAnalysis(currentAnalysis, currentTransaction, true);

			analysisRepository.updateAnalysis(beforeAnalysis, currentAnalysis, beforeTransaction, currentTransaction);

			return AnalysisDetailDto.of(currentAnalysis);
		}
		else {

			Analysis analysis = getAnalysis(userDetails, currentTransaction.getYear(), currentTransaction.getMonth());

			applyChangeToAnalysis(analysis, beforeTransaction, false);
			applyChangeToAnalysis(analysis, currentTransaction, true);

			analysisRepository.updateAnalysis(analysis, beforeTransaction, currentTransaction);

			return AnalysisDetailDto.of(analysis);
		}
	}

	@Transactional
	public AnalysisDetailDto addCombinedTransactiontoAnalysis(CustomUserDetails userDetails, Transaction mainTransaction) {
		Analysis analysis = getAnalysis(userDetails, mainTransaction.getYear(), mainTransaction.getMonth());
		Map<Integer, Analysis.DailyAmount> daily = analysis.getDaily();

		for(Transaction transaction: mainTransaction.getGroup()) {
			int day = transaction.getDay();
			String category = transaction.getCategory();
			int cost = transaction.getCost();

			if(cost >= 0) {
				int updatedIncome = Math.max(0, daily.get(day).getIncome() - cost);
				daily.get(day).setIncome(updatedIncome);

				if(daily.get(day).getIncome() == 0 && daily.get(day).getExpense() == 0) {
					daily.get(day).setIsValid(false);
				}
			}
			else {
				int updatedExpense = Math.max(0, daily.get(day).getExpense() + cost);
				daily.get(day).setExpense(updatedExpense);
				if(daily.get(day).getIncome() == 0 && daily.get(day).getExpense() == 0) {
					daily.get(day).setIsValid(false);
				}

				int updatedCategoryCost = Math.max(0, analysis.getCategory().get(category) + cost);
				analysis.getCategory().put(category, updatedCategoryCost);

				int updatedMonthTotal = Math.max(0, analysis.getMonthTotal() + cost);
				analysis.setMonthTotal(updatedMonthTotal);
			}
		}

		int day = mainTransaction.getDay();
		String category = mainTransaction.getCategory();
		int cost = mainTransaction.getCost();

		if(cost >= 0) {
			daily.get(day).setIncome(daily.get(day).getIncome() + cost);
			if(daily.get(day).getIncome() != 0) {
				daily.get(day).setIsValid(true);
			}
		}
		else { // make daily isvalid true
			cost = Math.abs(cost);
			daily.get(day).setExpense(daily.get(day).getExpense() + cost);
			if(daily.get(day).getExpense() != 0) {
				daily.get(day).setIsValid(true);
			}
			analysis.getCategory().put(category, analysis.getCategory().get(category) + cost);
			analysis.setMonthTotal(analysis.getMonthTotal() + cost);
		}

		analysisRepository.save(analysis);
		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto removeCombinedTransactionfromAnalysis(CustomUserDetails userDetails, Transaction mainTransaction) {
		Analysis analysis = getAnalysis(userDetails, mainTransaction.getYear(), mainTransaction.getMonth());
		Map<Integer, Analysis.DailyAmount> daily = analysis.getDaily();

		// undo main transaction
		int day = mainTransaction.getDay();
		String category = mainTransaction.getCategory();
		int cost = mainTransaction.getCost();

		if(cost >= 0) {
			daily.get(day).setIncome(daily.get(day).getIncome() - cost);
		}
		else { // make isvalid false
			daily.get(day).setExpense(daily.get(day).getExpense() + cost);
			if(daily.get(day).getIncome() == 0 && daily.get(day).getExpense() == 0) {
				daily.get(day).setIsValid(false);
			}
			analysis.getCategory().put(category, analysis.getCategory().get(category) + cost);
			analysis.setMonthTotal(analysis.getMonthTotal() + cost);
		}

		// apply sub transactions
		for(Transaction transaction: mainTransaction.getGroup()) {
			day = transaction.getDay();
			category = transaction.getCategory();
			cost = transaction.getCost();

			if(cost >= 0) {
				int updatedIncome = Math.max(0, daily.get(day).getIncome() + cost);
				daily.get(day).setIncome(updatedIncome);

				if(daily.get(day).getIncome() != 0) {
					daily.get(day).setIsValid(true);
				}
			}
			else {
				cost = Math.abs(cost);
				int updatedExpense = Math.max(0, daily.get(day).getExpense() + cost);
				daily.get(day).setExpense(updatedExpense);
				if(daily.get(day).getExpense() != 0) {
					daily.get(day).setIsValid(true);
				}

				int updatedCategoryCost = Math.max(0, analysis.getCategory().get(category) + cost);
				analysis.getCategory().put(category, updatedCategoryCost);

				int updatedMonthTotal = Math.max(0, analysis.getMonthTotal() + cost);
				analysis.setMonthTotal(updatedMonthTotal);
			}
		}

		analysisRepository.save(analysis);
		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto deleteCombinedTransactionfromAnalysis(CustomUserDetails userDetails, Transaction mainTransaction) {
		int year = mainTransaction.getYear();
		int month = mainTransaction.getMonth();

		Analysis analysis = getAnalysis(userDetails, year, month);
		Map<Integer, Analysis.DailyAmount> daily = analysis.getDaily();

		// undo main transaction
		int day = mainTransaction.getDay();
		String category = mainTransaction.getCategory();
		int cost = mainTransaction.getCost();

		if(cost >= 0) {
			daily.get(day).setIncome(daily.get(day).getIncome() - cost);
			if(daily.get(day).getIncome() == 0 && daily.get(day).getExpense() == 0) {
				daily.get(day).setIsValid(false);
			}
		}
		else {
			daily.get(day).setExpense(daily.get(day).getExpense() + cost);
			if(daily.get(day).getIncome() == 0 && daily.get(day).getExpense() == 0) {
				daily.get(day).setIsValid(false);
			}
			analysis.getCategory().put(category, analysis.getCategory().get(category) + cost);
			analysis.setMonthTotal(analysis.getMonthTotal() + cost);
		}

		// undo changes in payment method
		for(Transaction transaction: mainTransaction.getGroup()) {
			cost = transaction.getCost();
			String paymentMethod = transaction.getPaymentMethod();

			if(cost >= 0) {
				continue;
			}

			int updatedPaymentExpense = Math.max(0, analysis.getPaymentMethod().get(paymentMethod) + cost);
			if(updatedPaymentExpense == 0){
				analysis.getPaymentMethod().remove(paymentMethod);
			}
			else {
				analysis.getPaymentMethod().put(paymentMethod, updatedPaymentExpense);
			}
		}
		analysisRepository.save(analysis);
		return AnalysisDetailDto.of(analysis);
	}

	private void applyChangeToAnalysis(Analysis analysis, Transaction transaction, Boolean isAdded) {
		Integer cost = transaction.getCost();
		String category = transaction.getCategory();
		String paymentMethod = transaction.getPaymentMethod();
		int day = transaction.getDay();

		Analysis.DailyAmount dailyAmount = analysis.getDaily().get(day);
		Integer dailyIncome = dailyAmount.getIncome();
		Integer dailyExpense = dailyAmount.getExpense();
		Integer paymentExpense = Optional.ofNullable(analysis.getPaymentMethod().get(paymentMethod))
			.orElse(0);
		Integer categoryExpense = analysis.getCategory().get(category);

		if(cost < 0) {
			cost = isAdded? Math.abs(cost): cost;
			dailyExpense = Math.max(0, dailyExpense + cost);
			paymentExpense = Math.max(0, paymentExpense + cost);
			categoryExpense = Math.max(0, categoryExpense + cost);

			analysis.setMonthTotal(analysis.getMonthTotal() + cost);
			dailyAmount.setExpense(dailyExpense);
			analysis.getPaymentMethod().put(paymentMethod, paymentExpense);
			analysis.getCategory().put(category, categoryExpense);

			if(paymentExpense == 0) {
				analysis.getPaymentMethod().remove(paymentMethod);
			}
		}
		else {
			cost = isAdded? cost: -cost;
			dailyIncome = Math.max(0, dailyIncome + cost);
			dailyAmount.setIncome(dailyIncome);
		}

		dailyAmount.setIsValid(!(dailyIncome == 0 && dailyExpense == 0));
	}

	private Analysis getAnalysis(CustomUserDetails userDetails, Integer year, Integer month) {
		String id = String.format("%s_%d_%d", userDetails.id(), year, month);

		Analysis analysis = analysisRepository.findById(id)
			.orElseGet(() -> {
				Analysis newAnalysis = new Analysis();
				newAnalysis.setYear(year);
				newAnalysis.setMonth(month);
				newAnalysis.setId(id);
				return analysisRepository.save(newAnalysis);
			});

		return analysis;
	}
}
