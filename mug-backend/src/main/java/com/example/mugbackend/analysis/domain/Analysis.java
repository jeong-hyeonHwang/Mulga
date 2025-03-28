package com.example.mugbackend.analysis.domain;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analysis")
public class Analysis {
	@Id
	private String id; // Format: userId_year_month

	@Builder.Default
	private Integer year = 0;

	@Builder.Default
	private Integer month = 0;

	@Builder.Default
	private Integer monthTotal = 0;

	@Builder.Default
	private Map<String, Integer> category = new HashMap<>();

	@Builder.Default
	private Map<String, Integer> paymentMethod = new HashMap<>();

	@Builder.Default
	private Map<Integer, DailyAmount> daily = new HashMap<>();

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DailyAmount {
		private Integer income;
		private Integer expense;
		private Boolean isValid;
	}
}
