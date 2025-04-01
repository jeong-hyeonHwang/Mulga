package com.example.mugbackend.analysis.service;

import org.springframework.stereotype.Service;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.dto.AnalysisDetailDto;
import com.example.mugbackend.analysis.exception.AnalysisNotFoundException;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.user.dto.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AnalysisService {
	private final AnalysisRepository analysisRepository;

	public AnalysisDetailDto getAnalysisDetail(CustomUserDetails userDetails, Integer year, Integer month) {
		final int lastYear = (month == 1) ? year - 1 : year;
		final int lastMonth = (month == 1) ? 12 : month - 1;

		String thisMonthId = String.format("%s_%d_%d", userDetails.id(), year, month);
		String lastMonthId = String.format("%s_%d_%d", userDetails.id(), lastYear, lastMonth);

		Analysis lastMonthAnalysis = analysisRepository.findById(lastMonthId)
				.orElseGet(() -> Analysis.builder()
						.id(lastMonthId)
						.year(lastYear)
						.month(lastMonth)
						.monthTotal(0)
						.category(new HashMap<>())
						.paymentMethod(new HashMap<>())
						.daily(new HashMap<>())
						.build());
		Analysis thisMonthAnalysis = analysisRepository.findById(thisMonthId)
				.orElseThrow(AnalysisNotFoundException::new);

		return AnalysisDetailDto.of(lastMonthAnalysis, thisMonthAnalysis);
	}
}
