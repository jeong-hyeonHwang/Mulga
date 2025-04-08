package com.ilm.mulga.feature.transaction_detail

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ilm.mulga.presentation.model.TransactionDetailData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TransactionDetailViewModel : ViewModel() {

    private val _transactionDetailData = MutableStateFlow<TransactionDetailData?>(null)
    val transactionDetailData: StateFlow<TransactionDetailData?> = _transactionDetailData

    fun setTransactionDetail(data: TransactionDetailData) {
        _transactionDetailData.value = data
    }

    fun onEditTransaction(navController: NavController) {
        transactionDetailData.value?.let { data ->
            val json = Json.encodeToString(data)
            navController.currentBackStackEntry?.savedStateHandle?.set("editDataJson", json) // ✅ JSON 저장
            navController.navigate("transaction_edit/${data.id}")
        }
    }
}
