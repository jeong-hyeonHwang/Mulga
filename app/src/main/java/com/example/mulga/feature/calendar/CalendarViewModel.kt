package com.example.mulga.feature.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mulga.data.repository.TransactionRepository
import com.example.mulga.data.service.FakeTransactionService
import com.example.mulga.domain.model.MonthlyTransactionEntity
import com.example.mulga.presentation.mapper.toDailyTransactionData
import com.example.mulga.presentation.mapper.toDailyTransactionSummariesData
import com.example.mulga.presentation.mapper.toMonthlyTotalTransactionData
import com.example.mulga.presentation.model.DailyTransactionData
import com.example.mulga.presentation.model.DailyTransactionSummaryData
import com.example.mulga.presentation.model.MonthlyTotalTransactionData
import com.example.mulga.presentation.model.TransactionItemData
import com.example.mulga.presentation.model.type.Category
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

// UI 상태를 담는 데이터 클래스
data class CalendarUiState(
    // Domain Model (원본) 보관 – 필요하면 UI에서 활용
    val monthlyTransactionEntity: MonthlyTransactionEntity? = null,

    // Presentation Model로 변환한 데이터들
    val monthlyTotalData: MonthlyTotalTransactionData? = null,
    val dailySummariesData: List<DailyTransactionSummaryData>? = null,
    val dailyTransactionsData: List<DailyTransactionData>? = null,

    val selectedToggleIndex: Int = 1,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1, // Calendar.MONTH는 0부터 시작하므로 +1
    val totalSpending: String = "0",
)
class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    val fakeService: FakeTransactionService = FakeTransactionService()
    val repository: TransactionRepository = TransactionRepository(fakeService)

    // 도메인 모델을 불러와 Presentation Model로 변환하고 UI 상태에 저장하는 함수
    suspend fun loadAndConvertMonthlyData(year: Int, month: Int) {
        val monthlyEntity = repository.getMonthlyData(year, month)
        if (monthlyEntity != null) {
            // Domain Model을 Presentation Model로 변환
            val monthlyTotalData = monthlyEntity.toMonthlyTotalTransactionData()
            val dailySummariesData = monthlyEntity.toDailyTransactionSummariesData()
            val dailyTransactionsData = monthlyEntity.toDailyTransactionData()

            // UI 상태에 변환 결과 저장
            _uiState.value = _uiState.value.copy(
                monthlyTransactionEntity = monthlyEntity,
                monthlyTotalData = monthlyTotalData,
                dailySummariesData = dailySummariesData,
                dailyTransactionsData = dailyTransactionsData
            )
        }
    }

    fun onToggleOptionSelected(newIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedToggleIndex = newIndex)
    }

    suspend fun onPrevMonthClick() {
        val state = _uiState.value
        var year = state.currentYear
        var month = state.currentMonth

        if (month == 1) {
            month = 12
            year -= 1
        } else {
            month -= 1
        }
        _uiState.value = state.copy(currentYear = year, currentMonth = month)
        loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
    }

    suspend fun onNextMonthClick() {
        val state = _uiState.value
        var year = state.currentYear
        var month = state.currentMonth

        if (month == 12) {
            month = 1
            year += 1
        } else {
            month += 1
        }
        _uiState.value = state.copy(currentYear = year, currentMonth = month)
        loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
    }

    fun onPlusClick() {
        // plus 버튼 클릭 시 동작 처리 (예: 소비 내역 추가 화면 전환 등)
    }

    // TEST: 테스트 용도로 호출할 함수
    suspend fun test() {
        loadAndConvertMonthlyData(2025, 3)
    }
}
