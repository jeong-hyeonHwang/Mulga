package com.ilm.mulga.data.repository

import com.ilm.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import com.ilm.mulga.data.service.HomeService
import retrofit2.Response

class HomeRepository(private val service: HomeService) {
    suspend fun getHomeData(): HomeExpenseSummaryResponseDto? {
        val response: Response<HomeExpenseSummaryResponseDto> = service.getHomeTransactionSummary()
        return if (response.isSuccessful) response.body() else null
    }
}
