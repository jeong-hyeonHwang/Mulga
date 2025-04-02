package com.example.mugbackend.analysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisFullDetailDto {
	private String id; // Format: userId_year_month
	private Integer year;
	private Integer month;
	private Integer monthTotal;
	private Map<String, Integer> category;
	private Map<String, Integer> paymentMethod;
	private List<MonthlyTrendDto> monthlyTrend;
	private Map<Integer, Integer> lastMonthAccumulation;
	private Map<Integer, Integer> thisMonthAccumulation;
}