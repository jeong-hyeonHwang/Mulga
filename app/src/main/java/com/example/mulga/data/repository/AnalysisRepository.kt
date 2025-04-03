package com.example.mulga.data.repository

import com.example.mulga.data.service.AnalysisService
import com.example.mulga.domain.mapper.toDomainModel
import com.example.mulga.domain.model.AnalysisEntity

class AnalysisRepository(private val analysisService: AnalysisService) {

//    suspend fun getAnalysisData(year: Int, month: Int): AnalysisEntity? {
//        val response = analysisService.getAnalysis(year, month)
//        return if (response.isSuccessful) response.body()?.toDomainModel() else null
//    }
}
