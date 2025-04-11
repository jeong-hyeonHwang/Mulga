package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.response.HomeExpenseSummaryResponseDto
import com.ilm.mulga.data.service.HomeService
import kotlinx.serialization.json.Json
import retrofit2.Response

class FakeHomeService : HomeService {
    override suspend fun getHomeTransactionSummary(): Response<HomeExpenseSummaryResponseDto> {
        kotlinx.coroutines.delay(1000)
        val jsonResponse = """
                {
                    "monthTotal": 450000,
                    "remainingBudget": 0,
                    "lastTransaction": {
                        "_id": "67e241927be4585f3d4c7c47",
                        "year": 2025,
                        "month": 3,
                        "day": 25,
                        "isCombined": false,
                        "title": "아이스아메리카노",
                        "cost": -3000,
                        "category": "cafe",
                        "memo": "hello",
                        "vendor": "바나프레소",
                        "time": "2025-03-25T14:45:00Z",
                        "paymentMethod": "네이버페이",
                        "group": []
                    }
                }
            """.trimIndent()

        val response = Json.decodeFromString<HomeExpenseSummaryResponseDto>(jsonResponse)
        return Response.success(response)
    }
}
