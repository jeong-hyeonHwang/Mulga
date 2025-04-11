package com.example.mugbackend.analysis.dto;

import com.example.mugbackend.analysis.domain.Analysis;
import com.example.mugbackend.common.enumeration.CategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Builder
public record MonthlyTrendDto(
		int year,
		int month,
		int monthTotal
) {}