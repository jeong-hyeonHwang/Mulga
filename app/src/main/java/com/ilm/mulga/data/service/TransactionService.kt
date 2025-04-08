package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.request.TransactionRequestDto
import com.ilm.mulga.data.dto.response.MonthlyTransactionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.HTTP
import retrofit2.http.Path

interface TransactionService {
    @GET("/calendar/{year}/{month}")
    suspend fun getMonthlyTransactions(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<MonthlyTransactionResponseDto>

    @HTTP(method = "DELETE", path = "/transaction", hasBody = true)
    suspend fun deleteTransactions(
        @Body ids: List<String>
    ): Response<Unit>

    @POST("/transaction")
    suspend fun postTransaction(
        @Body request: TransactionRequestDto
    ): Response<Unit>
}