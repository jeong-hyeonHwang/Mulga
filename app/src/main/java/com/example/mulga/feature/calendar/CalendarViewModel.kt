package com.example.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

// UI 상태를 담는 데이터 클래스
data class CalendarUiState(
    val selectedToggleIndex: Int = 0,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1, // Calendar.MONTH는 0부터 시작하므로 +1
    val totalSpending: String = "0"
)

class CalendarViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    fun onToggleOptionSelected(newIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedToggleIndex = newIndex)
    }

    fun onPrevMonthClick() {
        val state = _uiState.value
        var year = state.currentYear
        var month = state.currentMonth

        if (month == 1) {
            // 1월에서 이전은 12월로 변경하고 연도를 감소시킴
            month = 12
            year -= 1
        } else {
            month -= 1
        }
        _uiState.value = state.copy(currentYear = year, currentMonth = month)
    }

    fun onNextMonthClick() {
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
    }

    fun onPlusClick() {
        // plus 버튼 클릭 시 수행할 동작 (예: 소비 내역 추가 화면 전환 등)
    }
}
