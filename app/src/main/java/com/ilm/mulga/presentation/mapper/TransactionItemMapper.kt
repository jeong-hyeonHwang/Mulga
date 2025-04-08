package com.ilm.mulga.presentation.mapper

import com.ilm.mulga.domain.model.TransactionEntity
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category


fun TransactionEntity.toPresentation(): TransactionDetailData {
    return TransactionDetailData(
        id = id,
        title = title,
        category = Category.fromBackendKey(category) ?: Category.ETC,
        vendor = vendor,
        time = time,
        cost = cost,
        memo = memo,
        paymentMethod = paymentMethod
    )
}