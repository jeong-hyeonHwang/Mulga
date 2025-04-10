package com.ilm.mulga.data.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MonthlyTrend(
    val year: Int,
    val month: Int,
    val monthTotal: Int
)

@Serializable
data class AnalysisDto(
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
