package com.ilm.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.TransactionRepository
import com.ilm.mulga.domain.model.MonthlyTransactionEntity
import com.ilm.mulga.presentation.mapper.toDailyTransactionData
import com.ilm.mulga.presentation.mapper.toDailyTransactionSummariesData
import com.ilm.mulga.presentation.mapper.toMonthlyTotalTransactionData
import com.ilm.mulga.presentation.model.DailyTransactionData
import com.ilm.mulga.presentation.model.DailyTransactionSummaryData
import com.ilm.mulga.presentation.model.MonthlyTotalTransactionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

// UI 상태를 담는 데이터 클래스
data class CalendarUiState(
    // Domain Model (원본) 보관 – 필요하면 UI에서 활용
    val monthlyTransactionEntity: MonthlyTransactionEntity? = null,

    // Presentation Model로 변환한 데이터들
    val monthlyTotalData: MonthlyTotalTransactionData? = MonthlyTotalTransactionData(),
    val dailySummariesData: List<DailyTransactionSummaryData>? = null,
    val dailyTransactionsData: List<DailyTransactionData>? = null,

    val selectedToggleIndex: Int = 0,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1, // Calendar.MONTH는 0부터 시작하므로 +1
    val totalSpending: String = "0",

    val selectedDate: LocalDate? = null
)

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val repository: TransactionRepository = TransactionRepository(RetrofitClient.transactionService)

    init {
        viewModelScope.launch {
            loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
            updateSelectedDateBasedOnToday(_uiState.value.currentYear, _uiState.value.currentMonth)
        }
    }

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
        loadAndConvertMonthlyData(year, month)

        // 현재 시스템 연월과 비교해서, 이번 달이 아니라면 마지막 날짜를 선택
        val systemCalendar = Calendar.getInstance()
        val systemYear = systemCalendar.get(Calendar.YEAR)
        val systemMonth = systemCalendar.get(Calendar.MONTH) + 1

        _uiState.value = updateSelectedDateBasedOnToday(year, month)
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
        loadAndConvertMonthlyData(year, month)

        _uiState.value = updateSelectedDateBasedOnToday(year, month)
    }
    // 선택된 날짜를 업데이트하는 함수
    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun onPlusClick() {
        // plus 버튼 클릭 시 동작 처리 (예: 소비 내역 추가 화면 전환 등)
    }

    private fun updateSelectedDateBasedOnToday(year: Int, month: Int): CalendarUiState {
        val systemCalendar = Calendar.getInstance()
        val systemYear = systemCalendar.get(Calendar.YEAR)
        val systemMonth = systemCalendar.get(Calendar.MONTH) + 1
        val today = LocalDate.now()
        val hasTodayData = _uiState.value.dailySummariesData?.any { it.date == today && it.isValid == true } ?: false

        return if (year == systemYear && month == systemMonth && hasTodayData) {
            _uiState.value.copy(selectedDate = today)
        } else {
            _uiState.value.copy(selectedDate = null)
        }
    }
}
