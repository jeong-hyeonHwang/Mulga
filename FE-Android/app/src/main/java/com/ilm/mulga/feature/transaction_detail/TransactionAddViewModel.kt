package com.ilm.mulga.feature.transaction_detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.dto.request.TransactionRequestDto
import com.ilm.mulga.data.dto.request.TransactionUpdateRequestDto
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.presentation.mapper.toDetailData
import com.ilm.mulga.presentation.model.TransactionDetailData
import com.ilm.mulga.util.extension.toIso8601String
import com.ilm.mulga.util.handler.GlobalErrorHandler
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime

class TransactionAddViewModel : ViewModel() {

    var isSuccess = mutableStateOf(false)
        private set

    var successResponse = mutableStateOf<TransactionDetailData?>(null)
        private set

    fun submitTransaction(
        title: String,
        cost: Int,
        category: String,
        vendor: String,
        paymentMethod: String,
        dateTime: LocalDateTime,
        memo: String
    ) {
        val request = TransactionRequestDto(
            year = dateTime.year,
            month = dateTime.monthValue,
            day = dateTime.dayOfMonth,
            title = title,
            cost = cost,
            category = category,
            time = dateTime.toIso8601String(),
            vendor = vendor,
            paymentMethod = paymentMethod,
            memo = memo
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.transactionService.postTransaction(request)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    // ✅ 성공 로그
                    Log.d("TransactionAdd", "Success: $response")
                    isSuccess.value = true
//                    successResponse.value = responseBody
                } else {
                    // ✅ 실패 시 서버에서 내려주는 에러 바디 파싱
                    val errorJson = response.errorBody()?.string()
                    val parsedError = Json.parseToJsonElement(errorJson ?: "").jsonObject
                    val errorCode = parsedError["code"]?.jsonPrimitive?.content
                    if (errorCode != null) {
                        GlobalErrorHandler.emitError(errorCode)
                    } else {
                        GlobalErrorHandler.emitError("UNKNOWN_ERROR")
                    }
                }
            } catch (e: Exception) {
                Log.e("TransactionAdd", "예외 발생: ${e.message}")
                e.printStackTrace()
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
                    Log.d("TransactionAdd", "Success: $responseBody")
                    isSuccess.value = true
                    successResponse.value = responseBody
                } else {
                    val errorJson = response.errorBody()?.string()
                    val parsedError = Json.parseToJsonElement(errorJson ?: "").jsonObject
                    val errorCode = parsedError["code"]?.jsonPrimitive?.content
                    if (errorCode != null) {
                        GlobalErrorHandler.emitError(errorCode)
                    } else {
                        GlobalErrorHandler.emitError("UNKNOWN_ERROR")
                    }
                }
            } catch (e: Exception) {
                Log.e("TransactionEdit", "예외 발생: ${e.message}")
                GlobalErrorHandler.emitError("NETWORK_ERROR")
            }
        }
    }
}
