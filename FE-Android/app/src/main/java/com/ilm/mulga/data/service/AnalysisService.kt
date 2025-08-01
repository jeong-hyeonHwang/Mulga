package com.ilm.mulga.data.service

import com.ilm.mulga.data.dto.response.AnalysisDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalysisService {
    @GET("analysis/{year}/{month}")
    suspend fun getAnalysis(
        @Path("year") year: Int,
        @Path("month") month: Int
    ): Response<AnalysisDto>
}