package com.ilm.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.repository.TransactionRepository
import com.ilm.mulga.domain.model.MonthlyTransactionEntity
import com.ilm.mulga.presentation.mapper.toDailyTransactionData
import com.ilm.mulga.presentation.mapper.toDailyTransactionSummariesData
import com.ilm.mulga.presentation.mapper.toMonthlyTotalTransactionData
import com.ilm.mulga.presentation.mapper.toPresentation
import com.ilm.mulga.presentation.model.DailyTransactionData
import com.ilm.mulga.presentation.model.DailyTransactionSummaryData
import com.ilm.mulga.presentation.model.MonthlyTotalTransactionData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.util.Calendar

// UI 상태를 담는 데이터 클래스
data class CalendarUiState(
    val monthlyTransactionEntity: MonthlyTransactionEntity? = null,
    val monthlyTotalData: MonthlyTotalTransactionData? = MonthlyTotalTransactionData(),
    val dailySummariesData: List<DailyTransactionSummaryData>? = null,
    val dailyTransactionsData: List<DailyTransactionData>? = null,
    val selectedToggleIndex: Int = 0,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val totalSpending: String = "0",
    val selectedDate: LocalDate? = null
)

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val repository: TransactionRepository =
        TransactionRepository(RetrofitClient.transactionService)

    init {
        viewModelScope.launch {
            // 초기 현재 연/월의 데이터 로드
            loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
            // 오늘 날짜가 있으면 선택
            _uiState.update { old ->
                updateSelectedDateBasedOnToday(old.currentYear, old.currentMonth, old)
            }
        }
    }

    suspend fun loadAndConvertMonthlyData(year: Int, month: Int) {
        val monthlyEntity = repository.getMonthlyData(year, month)
        if (monthlyEntity != null) {
            val monthlyTotalData = monthlyEntity.toMonthlyTotalTransactionData()
            val dailySummariesData = monthlyEntity.toDailyTransactionSummariesData()
            val dailyTransactionsData = monthlyEntity.toDailyTransactionData()

            _uiState.update { old ->
                old.copy(
                    monthlyTransactionEntity = monthlyEntity,
                    monthlyTotalData = monthlyTotalData,
                    dailySummariesData = dailySummariesData,
                    dailyTransactionsData = dailyTransactionsData
                )
            }
        }
    }

    fun onToggleOptionSelected(newIndex: Int) {
        _uiState.update { old ->
            old.copy(selectedToggleIndex = newIndex)
        }
    }

    suspend fun onPrevMonthClick() {
        val current = _uiState.value
        val (year, month) = currentYearMonth(current.currentYear, current.currentMonth, goNext = false)

        _uiState.update { old -> old.copy(currentYear = year, currentMonth = month) }
        loadAndConvertMonthlyData(year, month)
        _uiState.update { old ->
            updateSelectedDateBasedOnToday(year, month, old)
        }
    }

    suspend fun onNextMonthClick() {
        val current = _uiState.value
        val (year, month) = currentYearMonth(current.currentYear, current.currentMonth, goNext = true)

        _uiState.update { old -> old.copy(currentYear = year, currentMonth = month) }
        loadAndConvertMonthlyData(year, month)
        _uiState.update { old ->
            updateSelectedDateBasedOnToday(year, month, old)
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { old -> old.copy(selectedDate = date) }
    }

    fun onPlusClick() {
        // 처리 로직 구현
    }

    fun onTransactionClick(transactionId: String, navController: NavController) {
        val monthlyEntity = _uiState.value.monthlyTransactionEntity ?: return
        val transactionEntity = monthlyEntity.transactions
            .values
            .flatten()
            .find { it.id == transactionId }

        transactionEntity?.let { entity ->
            val detailData = entity.toPresentation()
            val jsonData = Json.encodeToString(detailData)
            val encodedData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())

            navController.navigate("transaction_detail?data=$encodedData")
        }
    }

    fun deleteTransactionItems(deletedIds: Set<String>) {
        viewModelScope.launch {
            repository.deleteTransactions(deletedIds)
            loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
        }
    }

    private fun currentYearMonth(year: Int, month: Int, goNext: Boolean): Pair<Int, Int> {
        var y = year
        var m = month
        if (goNext) {
            if (m == 12) {
                y += 1
                m = 1
            } else {
                m += 1
            }
        } else {
            if (m == 1) {
                y -= 1
                m = 12
            } else {
                m -= 1
            }
        }
        return y to m
    }

    private fun updateSelectedDateBasedOnToday(
        year: Int,
        month: Int,
        old: CalendarUiState
    ): CalendarUiState {
        val systemCalendar = Calendar.getInstance()
        val systemYear = systemCalendar.get(Calendar.YEAR)
        val systemMonth = systemCalendar.get(Calendar.MONTH) + 1
        val today = LocalDate.now()
        val hasTodayData = old.dailySummariesData?.any { it.date == today && it.isValid } ?: false

        return if (year == systemYear && month == systemMonth && hasTodayData) {
            old.copy(selectedDate = today)
        } else {
            old.copy(selectedDate = null)
        }
    }
}
