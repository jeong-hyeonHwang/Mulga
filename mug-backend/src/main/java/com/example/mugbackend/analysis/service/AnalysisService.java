package com.example.mugbackend.analysis.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.dto.AnalysisDetailDto;
import com.example.mugbackend.analysis.exception.AnalysisNotFoundException;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

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

	@Transactional
	public AnalysisDetailDto addTransactionToAnalysis(CustomUserDetails userDetails, Transaction transaction) {
		Analysis analysis = getAnalysisById(userDetails, transaction.getYear(), transaction.getMonth());

		applyChangeToAnalysis(analysis, transaction, true);

		analysisRepository.updateAnalysis(analysis, transaction);

		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto removeTransactionFromAnalysis(CustomUserDetails userDetails, Transaction transaction) {
		Analysis analysis = getAnalysisById(userDetails, transaction.getYear(), transaction.getMonth());

		applyChangeToAnalysis(analysis, transaction, false);

		analysisRepository.updateAnalysis(analysis, transaction);

		return AnalysisDetailDto.of(analysis);
	}

	@Transactional
	public AnalysisDetailDto alterTransactionToAnalysis(CustomUserDetails userDetails, Transaction beforeTransaction, Transaction currentTransaction) {
		if(!beforeTransaction.getYear().equals(currentTransaction.getYear()) || !(beforeTransaction.getMonth().equals(currentTransaction.getMonth()))) {
			Analysis beforeAnalysis = getAnalysisById(userDetails, beforeTransaction.getYear(), beforeTransaction.getMonth());
			Analysis currentAnalysis = getAnalysisById(userDetails, currentTransaction.getYear(), currentTransaction.getMonth());

			applyChangeToAnalysis(beforeAnalysis, beforeTransaction, false);
			applyChangeToAnalysis(currentAnalysis, currentTransaction, true);

			analysisRepository.updateAnalysis(beforeAnalysis, currentAnalysis, beforeTransaction, currentTransaction);

			return AnalysisDetailDto.of(currentAnalysis);
		}
		else {

			Analysis analysis = getAnalysisById(userDetails, currentTransaction.getYear(), currentTransaction.getMonth());

			applyChangeToAnalysis(analysis, beforeTransaction, false);
			applyChangeToAnalysis(analysis, currentTransaction, true);

			analysisRepository.updateAnalysis(analysis, beforeTransaction, currentTransaction);

			return AnalysisDetailDto.of(analysis);
		}
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



	private Analysis getAnalysisById(CustomUserDetails userDetails, Integer year, Integer month) {
		String id = String.format("%s_%d_%d", userDetails.id(), year, month);

		Analysis analysis = analysisRepository.findById(id)
			.orElseThrow(AnalysisNotFoundException::new);

		return analysis;
	}
}
