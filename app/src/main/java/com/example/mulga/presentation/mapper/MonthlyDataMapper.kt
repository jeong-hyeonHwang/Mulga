package com.example.mulga.presentation.mapper

import com.example.mulga.domain.model.MonthlyTransactionEntity
import com.example.mulga.domain.model.TransactionEntity
import com.example.mulga.presentation.model.DailyTransactionData
import com.example.mulga.presentation.model.DailyTransactionSummaryData
import com.example.mulga.presentation.model.MonthlyTotalTransactionData
import com.example.mulga.presentation.model.TransactionItemData
import com.example.mulga.presentation.model.type.Category
import com.example.mulga.util.extension.formatTimeToHourMinute
import com.example.mulga.util.extension.formatTimeToHourMinuteForMain
import java.time.LocalDate
import java.time.YearMonth

// Domain Model → Presentation Model 변환 확장 함수

// 1. 월 전체 요약 정보 변환
fun MonthlyTransactionEntity.toMonthlyTotalTransactionData(): MonthlyTotalTransactionData {
    return MonthlyTotalTransactionData(
        year = this.year,
        month = this.month,
        monthTotal = this.monthTotal.toInt() // 필요에 따라 Long을 Int 또는 String으로 포맷팅
    )
}

// 2. 일자별 요약 정보 변환 (Map을 List로 변환하면서 일자 순서대로 정렬)
fun MonthlyTransactionEntity.toDailyTransactionSummariesData(): List<DailyTransactionSummaryData> {
    return dailySummaries.entries.sortedBy { it.key }.map { (day, summaryEntity) ->
        DailyTransactionSummaryData(
            date = createValidDate(year, this.month, day),
            isValid = summaryEntity.isValid,
            income = summaryEntity.income.toInt(),
            expense = summaryEntity.expense.toInt()
        )
    }
}

// 3. 일자별 거래 내역 변환
fun MonthlyTransactionEntity.toDailyTransactionData(): List<DailyTransactionData> {
    return transactions.entries.sortedByDescending { it.key }.map { (day, transactionEntities) ->
        DailyTransactionData(
            date = createValidDate(year, this.month, day),
            transactions = transactionEntities.map { it.toTransactionItemData() }
        )
    }
}

// 4. 개별 거래 내역을 TransactionItemData로 변환 (캘린더용)
fun TransactionEntity.toTransactionItemData(): TransactionItemData {
    // title, vendor, paymentMethod은 null 또는 빈 문자열일 수 있으므로 안전하게 확인합니다.
    val hasTitle = this.title.isNotEmpty()
    val hasVendor = this.vendor.isNotEmpty()

    // title은 존재하면 title, 없으면 vendor가 있으면 vendor, 둘 다 없으면 "-"
    val newTitle = when {
        hasTitle -> this.title
        !hasTitle && hasVendor -> this.vendor
        else -> "-"
    }

    // subtitle은 title과 vendor 모두 있을 때 "paymentMethod | vendor", 그 외에는 paymentMethod만 사용
    val newSubtitle = if (hasTitle && hasVendor) {
        "${this.paymentMethod} | ${this.vendor}"
    } else {
        this.paymentMethod ?: ""
    }

    return TransactionItemData(
        category = Category.fromBackendKey(this.category),
        title = newTitle,
        subtitle = newSubtitle,
        price = this.cost.toString(), // 필요시 포맷팅 추가 가능
        time = formatTimeToHourMinute(this.time)
    )
}

fun TransactionEntity.toTransactionItemDataForMain(): TransactionItemData {
    // title, vendor, paymentMethod은 null 또는 빈 문자열일 수 있으므로 안전하게 확인합니다.
    val hasTitle = !this.title.isNullOrEmpty()
    val hasVendor = !this.vendor.isNullOrEmpty()

    // title은 존재하면 title, 없으면 vendor가 있으면 vendor, 둘 다 없으면 "-"
    val newTitle = when {
        hasTitle -> this.title!!
        !hasTitle && hasVendor -> this.vendor!!
        else -> "-"
    }

    // subtitle은 title과 vendor 모두 있을 때 "paymentMethod | vendor", 그 외에는 paymentMethod만 사용
    val newSubtitle = if (hasTitle && hasVendor) {
        "${this.paymentMethod} | ${this.vendor}"
    } else {
        this.paymentMethod ?: ""
    }

    return TransactionItemData(
        category = Category.fromBackendKey(this.category),
        title = newTitle,
        subtitle = newSubtitle,
        price = this.cost.toString(), // 필요시 포맷팅 추가 가능
        time = formatTimeToHourMinuteForMain(this.time)
    )
}

fun createValidDate(year: Int, month: Int, day: Int): LocalDate {
    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
    val validDay = if (day > daysInMonth) daysInMonth else day
    return LocalDate.of(year, month, validDay)
}
