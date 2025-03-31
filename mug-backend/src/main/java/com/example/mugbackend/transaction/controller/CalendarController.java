package com.example.mugbackend.transaction.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;
import com.example.mugbackend.transaction.dto.MonthlyTransactionDto;
import com.example.mugbackend.transaction.service.TransactionService;
import com.example.mugbackend.user.domain.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController{

	private final TransactionService transactionService;

	@GetMapping("/{year}/{month}")
	public ResponseEntity<?> getMonthlyTransactions(
		User user,
		@PathVariable int year,
		@PathVariable int month) {

//		System.out.println("userId: " + user.getId()); // FIXME: null로 나옴

		
//		int monthTotal = transactionService.getMonthTotal(user.getId(), year, month);
//		Map<Integer, Analysis.DailyAmount> daily = transactionService.getDaily(user.getId(), year, month);
//		LinkedHashMap<Integer, List<Transaction>> transactions = transactionService.getTransactions(user.getId(), year, month);

		int monthTotal = transactionService.getMonthTotal(id, year, month);
		Map<Integer, Analysis.DailyAmount> daily = transactionService.getDaily(id, year, month);
		LinkedHashMap<Integer, List<Transaction>> transactions = transactionService.getTransactionsByTimeDESC(id, year, month);

		MonthlyTransactionDto dto = MonthlyTransactionDto.builder()
										.monthTotal(monthTotal)
										.year(year)
										.month(month)
										.daily(daily)
										.transactions(transactions)
										.build();

		return ResponseEntity.ok(dto);
	}
}
