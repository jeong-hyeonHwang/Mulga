package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface HomeService {
    @GET("/main")
    suspend fun getHomeTransactionSummary(): Response<HomeExpenseSummaryResponseDto>
}