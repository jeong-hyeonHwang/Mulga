package com.example.mugbackend.analysis.repository;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.transaction.domain.Transaction;

public interface CustomAnalysisRepository {
	void updateAnalysis(Analysis analysis, Transaction transaction);
	void updateAnalysis(Analysis beforeAnalysis, Analysis currentAnalysis, Transaction beforeTransaction, Transaction currentTransaction);
	void updateAnalysis(Analysis analysis, Transaction beforeTransaction, Transaction currentTransaction);
}
