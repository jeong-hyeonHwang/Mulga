package com.ilm.mulga.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.ilm.mulga.R
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

sealed class UiEvent {
    data class ShowToast(val messageResId: Int) : UiEvent()
}

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val repository: TransactionRepository = TransactionRepository(RetrofitClient.transactionService)

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // UI 이벤트를 전달할 SharedFlow
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

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
        // plus 버튼 클릭 시 동작 처리 (예: 소비 내역 추가 화면 전환 등)
        viewModelScope.launch {
            _navigationEvent.emit("transaction_add")
        }
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

            if (entity.isCombined) {
                navController.navigate("transaction_combine_detail?data=$encodedData")
            } else {
                navController.navigate("transaction_detail?data=$encodedData")
            }
        }
    }

    fun isValidForDelete(deletedIds: LinkedHashSet<String>): Boolean {
        if (deletedIds.isEmpty()) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(R.string.toast_delete_minimum_selection_message))
            }
            return false
        }
        return true
    }

    fun isValidForCombine(combineIds: LinkedHashSet<String>) : Boolean{
        val monthlyEntity = _uiState.value.monthlyTransactionEntity ?: return false

        // 삭제할 항목들 필터링 (선택된 ID와 일치하는 항목)
        val transactionsToDelete = monthlyEntity.transactions
            .values
            .flatten()
            .filter { combineIds.contains(it.id) }

        // 만약 그 중 하나라도 isCombined가 true이면
        if (transactionsToDelete.any { it.isCombined }) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(R.string.toast_merge_already_combined_message))
            }
            return false
        }

        if (combineIds.size <= 1) {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.ShowToast(R.string.toast_merge_minimum_selection_message))
            }
            return false
        }
        return true
    }

    fun deleteTransactionItems(deletedIds: LinkedHashSet<String>) {
        viewModelScope.launch {
            repository.deleteTransactions(deletedIds)
            loadAndConvertMonthlyData(_uiState.value.currentYear, _uiState.value.currentMonth)
        }
    }

    fun combineTransactionItems(combineIds: LinkedHashSet<String>) {
        viewModelScope.launch {
            val mainTransactionId = combineIds.firstOrNull()
            val combineTransactionIds = combineIds.drop(1).toCollection(LinkedHashSet())
            if (mainTransactionId != null) {
                repository.combineTransactions(mainTransactionId, combineTransactionIds)
            }
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
