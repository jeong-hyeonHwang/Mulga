package com.ilm.mulga.domain.mapper

import com.ilm.mulga.data.dto.response.DailyTransactionSummaryDto
import com.ilm.mulga.data.dto.response.MonthlyTransactionResponseDto
import com.ilm.mulga.data.dto.response.TransactionDto
import com.ilm.mulga.domain.model.DailyTransactionSummaryEntity
import com.ilm.mulga.domain.model.MonthlyTransactionEntity
import com.ilm.mulga.domain.model.TransactionEntity

// DailyDto -> DailySummary 변환
fun DailyTransactionSummaryDto.toDailySummary(): DailyTransactionSummaryEntity {
    return DailyTransactionSummaryEntity(
        isValid = this.isValid,
        income = this.income,
        expense = this.expense
    )
}

// TransactionDto -> Transaction 변환
fun TransactionDto.toTransaction(): TransactionEntity {
    return TransactionEntity(
        id = this._id,
        year = this.year,
        month = this.month,
        day = this.day,
        isCombined = this.isCombined,
        title = this.title,
        cost = this.cost,
        category = this.category,
        memo = this.memo,
        vendor = this.vendor,
        time = this.time,
        paymentMethod = this.paymentMethod,
        group = this.group.map { it.toTransaction() }
    )
}

// MonthlyResponseDto -> MonthData 변환
fun MonthlyTransactionResponseDto.toDomainModel(): MonthlyTransactionEntity {
    val dailySummaries = daily.mapKeys { it.key.toInt() }
        .mapValues { (_, dailyDto) -> dailyDto.toDailySummary() }

    val transactionsDomain = transactions.mapKeys { it.key.toInt() }
        .mapValues { (_, transactionsDto) ->
            transactionsDto.map { it.toTransaction() }
        }

    return MonthlyTransactionEntity(
        monthTotal = this.monthTotal,
        year = this.year,
        month = this.month,
        dailySummaries = dailySummaries,
        transactions = transactionsDomain
    )
}
