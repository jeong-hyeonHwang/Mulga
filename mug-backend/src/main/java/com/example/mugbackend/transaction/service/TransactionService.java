package com.example.mugbackend.transaction.service;

import com.example.mugbackend.analysis.repository.AnalysisRepository;
import com.example.mugbackend.analysis.domain.Analysis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AnalysisRepository analysisRepository;

    public int getMonthTotal(String userId, int year, int month) {
        String id = userId + "_" + year + "_" + month;
        return analysisRepository.findById(id)
                .map(Analysis::getMonthTotal)
                .orElse(0);
    }

    public Map getDaily(String userId, int year, int month) {
        String id = userId + "_" + year + "_" + month;
        return analysisRepository.findById(id)
                .map(Analysis::getDaily)
                .orElse(Collections.emptyMap());
    }

}
