package com.example.mugbackend.analysis.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class AnalysisNotFoundException extends MulgaException {
	public AnalysisNotFoundException() {
		super(AnalysisErrorCode.ANALYSIS_NOT_FOUND);
	}
}
