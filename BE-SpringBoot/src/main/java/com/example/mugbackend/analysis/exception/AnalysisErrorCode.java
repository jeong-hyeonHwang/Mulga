package com.example.mugbackend.analysis.exception;

import com.example.mugbackend.common.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AnalysisErrorCode implements ErrorCode {
	ANALYSIS_NOT_FOUND("ANALYSIS_1000"),
	ANALYSIS_ACCESS_DENIED("ANALYSIS_1001");
	private final String code;
}
