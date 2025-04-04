package com.ilm.mulga.domain.mapper

import com.ilm.mulga.data.dto.response.DailyTransactionSummaryDto
import com.ilm.mulga.data.dto.response.MonthlyTransactionResponseDto
import com.ilm.mulga.data.dto.response.TransactionDto
import com.ilm.mulga.domain.model.DailyTransactionSummaryEntity
import com.ilm.mulga.domain.model.MonthlyTransactionEntity
import com.ilm.mulga.domain.model.TransactionEntity
import java.time.LocalDateTime

// DailyDto -> DailySummary 변환
fun DailyTransactionSummaryDto.toDailySummary(): DailyTransactionSummaryEntity {
    return DailyTransactionSummaryEntity(
        isValid = this.isValid,
        income = this.income,
        expense = this.expense
    )
}

// TransactionDto -> Transaction 변환
fun TransactionDto.toDomain(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        userId = this.userId,
        year = this.year,
        month = this.month,
        day = this.day,
        isCombined = this.isCombined,
        title = this.title,
        cost = this.cost,
        category = this.category,
        memo = this.memo,
        vendor = this.vendor,
        time =  LocalDateTime.parse(this.time),
        paymentMethod = this.paymentMethod,
        group = this.group.map { it.toDomain() }
    )
}

// MonthlyResponseDto -> MonthData 변환
fun MonthlyTransactionResponseDto.toDomain(): MonthlyTransactionEntity {
    val dailySummaries = daily.mapKeys { it.key.toInt() }
        .mapValues { (_, dailyDto) -> dailyDto.toDailySummary() }

    val transactionsDomain = transactions.mapKeys { it.key.toInt() }
        .mapValues { (_, transactionsDto) ->
            transactionsDto.map { it.toDomain() }
        }

    return MonthlyTransactionEntity(
        monthTotal = this.monthTotal,
        year = this.year,
        month = this.month,
        dailySummaries = dailySummaries,
        transactions = transactionsDomain
    )
}
