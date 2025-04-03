package com.example.mulga.data.service

import com.example.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeService {
    @GET("/main")
    suspend fun getHomeTransactionSummary(): Response<HomeExpenseSummaryResponseDto>
}