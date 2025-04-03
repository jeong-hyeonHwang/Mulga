package com.example.mulga.domain.model

import com.example.mulga.data.dto.response.MonthlyTrend

data class AnalysisEntity(
    val id: String,
    val year: Int,
    val month: Int,
    val monthTotal: Int,
    val category: Map<String, Int>,
    val paymentMethod: Map<String, Int>,
    val monthlyTrend: List<MonthlyTrend>,
    val lastMonthAccumulation: Map<String, Int>,
    val thisMonthAccumulation: Map<String, Int>
)
