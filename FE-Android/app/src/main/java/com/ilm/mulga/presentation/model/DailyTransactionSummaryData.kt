package com.ilm.mulga.presentation.model

import java.time.LocalDate


/**
 * 달력에 표시할 데이터 예시용
 * - date: 날짜 (1~31)
 * - expense: 지출(음수)
 * - income: 수입(양수)
 */
data class DailyTransactionSummaryData(
    var date: LocalDate,
    var isValid: Boolean,
    var income: Int,
    var expense: Int
)