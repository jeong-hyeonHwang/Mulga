package com.ilm.mulga.feature.transaction_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.domain.model.TransactionEntity
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.presentation.model.type.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransactionDetailViewModel : ViewModel() {

    private val _transactionDetailData = MutableStateFlow<TransactionDetailData?>(null)
    val transactionDetailData: StateFlow<TransactionDetailData?> = _transactionDetailData

    fun setTransactionDetail(data: TransactionDetailData) {
        _transactionDetailData.value = data
    }

    fun onEditTransaction() {
        // 편집 버튼 클릭 시 로직 처리
    }
}
