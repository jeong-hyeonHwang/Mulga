package com.example.mulga.feature.home

import com.example.mulga.presentation.model.HomeExpenseSummaryData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mulga.data.network.RetrofitClient
import com.example.mulga.data.repository.HomeRepository
import com.example.mulga.domain.model.toDomain
import com.example.mulga.presentation.model.toPresentation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeExpenseSummaryData())
    val uiState: StateFlow<HomeExpenseSummaryData> = _uiState

    private val repository: HomeRepository = HomeRepository(RetrofitClient.homeService)

    init {
        viewModelScope.launch {
            loadAndConvertHomeData()
        }
    }

    // 도메인 모델을 불러와 Presentation Model로 변환하고 UI 상태에 저장하는 함수
    private suspend fun loadAndConvertHomeData() {
        val homeExpenseSummaryEntity = repository.getHomeData()
        if (homeExpenseSummaryEntity != null) {
            // 도메인 모델 및 Presentation 모델로 매핑하는 함수 사용 (예: toDomain()와 toPresentation())
            val presentationModel = homeExpenseSummaryEntity.toDomain().toPresentation()
            _uiState.value = presentationModel
        }
    }
}
