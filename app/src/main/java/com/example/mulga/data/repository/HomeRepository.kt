package com.example.mulga.data.repository

import com.example.mulga.BuildConfig
import com.example.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import com.example.mulga.data.service.HomeService
import retrofit2.Response

class HomeRepository(private val service: HomeService) {
    suspend fun getHomeData(): HomeExpenseSummaryResponseDto? {
        val response: Response<HomeExpenseSummaryResponseDto> = service.getHomeTransactionSummary()
        return if (response.isSuccessful) response.body() else null
    }
}
