package com.example.mugbackend.analysis.exception;

import com.example.mugbackend.common.exception.MulgaException;

public class AnalysisAccessDeniedException extends MulgaException {
	public AnalysisAccessDeniedException() {
		super(AnalysisErrorCode.ANALYSIS_ACCESS_DENIED);
	}
}
