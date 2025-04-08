package com.ilm.mulga.feature.transaction_detail

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.dto.request.TransactionRequestDto
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.util.extension.toIso8601String
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDateTime

class TransactionAddViewModel : ViewModel() {

    var isSuccess = mutableStateOf(false)
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
                    // ✅ 성공 로그
                    Log.d("TransactionAdd", "Success: ${response.body()}")
                    isSuccess.value = true
                } else {
                    // ✅ 실패 시 서버에서 내려주는 에러 바디 파싱
                    val errorJson = response.errorBody()?.string()
                    val parsedError = Json.parseToJsonElement(errorJson ?: "").jsonObject
                    val errors = parsedError["errors"]?.jsonObject

                    errors?.forEach { (field, messages) ->
                        Log.e("TransactionAdd", "[$field] ${(messages.jsonArray.joinToString { it.jsonPrimitive.content })}")
                    }
                }
            } catch (e: Exception) {
                Log.e("TransactionAdd", "예외 발생: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
