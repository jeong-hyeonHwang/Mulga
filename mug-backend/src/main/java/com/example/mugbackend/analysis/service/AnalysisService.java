package com.example.mugbackend.analysis.service;

import org.springframework.stereotype.Service;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.analysis.dto.AnalysisDetailDto;
import com.example.mugbackend.analysis.exception.AnalysisNotFoundException;
import com.example.mugbackend.analysis.repository.AnalysisRepository;
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
	private Analysis getAnalysisById(CustomUserDetails userDetails, Integer year, Integer month) {
		String id = String.format("%s_%d_%d", userDetails.id(), year, month);

		Analysis analysis = analysisRepository.findById(id)
			.orElseThrow(AnalysisNotFoundException::new);

		return analysis;
	}
}
