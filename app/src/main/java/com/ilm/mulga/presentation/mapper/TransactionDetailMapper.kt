package com.ilm.mulga.presentation.mapper

import com.ilm.mulga.presentation.model.DailyTransactionData
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.presentation.model.TransactionDetailData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * List<TransactionDetailData>를 날짜 기준으로 그룹화하여 DailyTransactionData 리스트로 변환하는 확장 함수.
 */
fun List<TransactionDetailData>.toDailyTransactionData(): List<DailyTransactionData> {
    // time의 LocalDate 값을 기준으로 그룹화
    val grouped: Map<LocalDate, List<TransactionDetailData>> = this.groupBy { detail ->
        detail.time.toLocalDate()
    }
    // 그룹별로 DailyTransactionData로 변환
    return grouped.map { (date, detailsForDate) ->
        DailyTransactionData(
            date = date,
            transactions = detailsForDate.map { it.toTransactionItemDataForMain() }
        )
    }.sortedByDescending { it.date }
}

/**
 * TransactionDetailData를 TransactionItemData로 변환하는 확장 함수.
 */
fun TransactionDetailData.toTransactionItemDataForMain(): TransactionItemData {
    // title과 vendor는 non-null이므로 isNotEmpty() 사용
    val hasTitle = this.title.isNotEmpty()
    val hasVendor = this.vendor.isNotEmpty()

    val newTitle = when {
        hasTitle -> this.title
        !hasTitle && hasVendor -> this.vendor
        else -> "-"
    }

    val newSubtitle = if (this.group.isNotEmpty()) {
        "# 합쳤어요!"
    } else if (hasTitle && hasVendor) {
        "${this.paymentMethod} | ${this.vendor}"
    } else {
        this.paymentMethod
    }

    // 예시: 시간을 "HH:mm" 형식으로 포맷하는 함수 (자신의 구현에 맞게 수정)
    fun formatTimeToHourMinuteForMain(time: java.time.LocalDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return time.format(formatter)
    }

    return TransactionItemData(
        id = this.id,
        category = this.category, // Category 타입 그대로 사용, 필요 시 Category.fromBackendKey() 적용
        title = newTitle,
        subtitle = newSubtitle,
        price = this.cost.toString(),
        time = formatTimeToHourMinuteForMain(this.time),
        isCombined = this.group.isNotEmpty()
    )
}