package com.example.mulga.data.service

import com.example.mulga.data.dto.response.MonthlyTransactionResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TransactionService {
    @GET("/calendar/{year}/{month}")
    suspend fun getMonthlyTransactions(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<MonthlyTransactionResponseDto>
}