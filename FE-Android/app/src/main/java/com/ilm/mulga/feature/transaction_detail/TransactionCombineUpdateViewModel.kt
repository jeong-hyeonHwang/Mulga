package com.ilm.mulga.feature.transaction_detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.dto.request.TransactionUpdateRequestDto
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.domain.mapper.toDomain
import com.ilm.mulga.presentation.mapper.toDetailData
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.util.extension.toIso8601String
import com.ilm.mulga.util.handler.GlobalErrorHandler
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime

class TransactionCombineUpdateViewModel : ViewModel() {

    var isUncombineSuccess = mutableStateOf(false)
        private set

    var isSuccess = mutableStateOf(false)
        private set

    var successResponse = mutableStateOf<TransactionDetailData?>(null)
        private set

    fun uncombineTransaction(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.transactionService.uncombineTransaction(id)
                if (response.isSuccessful) {
                    val data = response.body()?.map { it.toDomain() } ?: emptyList()
                    // 🔸 이 리스트를 활용할 계획이 있다면 변수로 따로 보관해도 됨
                    isUncombineSuccess.value = true
                    Log.d("UncombineTransaction", "Success")
                } else {
                    val errorJson = response.errorBody()?.string()
                    val parsedError = Json.parseToJsonElement(errorJson ?: "").jsonObject
                    val errorCode = parsedError["code"]?.jsonPrimitive?.content
                    GlobalErrorHandler.emitError(errorCode ?: "UNKNOWN_ERROR")
                }
            } catch (e: Exception) {
                Log.e("UncombineTransaction", "예외 발생: ${e.message}")
                GlobalErrorHandler.emitError("NETWORK_ERROR")
            }
        }
    }

    fun patchTransaction(
        id: String,
        title: String,
        cost: Int,
        category: String?,
        vendor: String,
        paymentMethod: String,
        dateTime: LocalDateTime,
        memo: String
    ) {
        val request = TransactionUpdateRequestDto(
            id = id,
            year = dateTime.year,
            month = dateTime.monthValue,
            day = dateTime.dayOfMonth,
            title = title,
            cost = cost,
            category = category ?: "",
            time = dateTime.toIso8601String(),
            vendor = vendor,
            paymentMethod = paymentMethod,
            memo = memo
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.transactionService.patchTransaction(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.toDetailData()
                    isSuccess.value = true
                    successResponse.value = responseBody
                    Log.d("TransactionEdit", "Success: $responseBody")
                } else {
                    val errorJson = response.errorBody()?.string()
                    val parsedError = Json.parseToJsonElement(errorJson ?: "").jsonObject
                    val errorCode = parsedError["code"]?.jsonPrimitive?.content
                    GlobalErrorHandler.emitError(errorCode ?: "UNKNOWN_ERROR")
                }
            } catch (e: Exception) {
                Log.e("TransactionEdit", "예외 발생: ${e.message}")
                GlobalErrorHandler.emitError("NETWORK_ERROR")
            }
        }
    }
}
