package com.ilm.mulga.presentation.mapper

import com.ilm.mulga.data.dto.response.TransactionDto
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun TransactionDto.toDetailData(): TransactionDetailData {
    return TransactionDetailData(
        id = id,
        title = title,
        category = Category.fromBackendKey(category) ?: Category.ETC,
        vendor = vendor,
        time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
        cost = cost,
        memo = memo,
        paymentMethod = paymentMethod,
        group = group.map { it.toDetailData() }
    )
}