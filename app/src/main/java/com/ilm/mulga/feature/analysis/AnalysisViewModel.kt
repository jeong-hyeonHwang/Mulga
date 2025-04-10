package com.ilm.mulga.feature.analysis

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import com.ilm.mulga.feature.analysis.components.CategoryItemRaw
import java.time.LocalDate
import androidx.lifecycle.viewModelScope
import com.ilm.mulga.data.network.RetrofitClient
import com.ilm.mulga.data.dto.response.AnalysisDto
import kotlinx.coroutines.launch
import retrofit2.Response

class AnalysisViewModel : ViewModel() {
    // State to manage loading, success, and error states
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    // Data states for holding the response from the API
    private val _analysisData = mutableStateOf<AnalysisDto?>(null)
    val analysisData: State<AnalysisDto?> = _analysisData

    // Data for the UI
    private val _total = mutableIntStateOf(0)
    val total: State<Int> = _total

    private val _slices = mutableStateOf<List<Int>>(emptyList())
    val slices: State<List<Int>> = _slices

    private val _items = mutableStateOf<List<CategoryItemRaw>>(emptyList())
    val items: State<List<CategoryItemRaw>> = _items

    private val _line1Data = mutableStateOf<List<Float>>(emptyList())  // For line1 data
    val line1Data: State<List<Float>> = _line1Data

    private val _line2Data = mutableStateOf<List<Float>>(emptyList())  // For line2 data
    val line2Data: State<List<Float>> = _line2Data

    // Private mutable state for selected year and month
    private val _selectedYear = mutableIntStateOf(LocalDate.now().year)  // Default current year
    private val _selectedMonth = mutableIntStateOf(LocalDate.now().monthValue)  // Default current month

    val selectedYear: State<Int> = _selectedYear
    val selectedMonth: State<Int> = _selectedMonth

    // Function to set selected year
    fun setSelectedYear(year: Int) {
        _selectedYear.intValue = year
    }

    // Function to set selected month
    fun setSelectedMonth(month: Int) {
        _selectedMonth.intValue = month
    }

    // Function to fetch data from the API
    fun fetchAnalysisData(year: Int, month: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Make the API call
                val response: Response<AnalysisDto> = RetrofitClient.apiAnalysisService.getAnalysis(year, month)
                if (response.isSuccessful) {
                    // Handle successful response
                    _analysisData.value = response.body()
                    _errorMessage.value = null
                    _total.intValue = _analysisData.value?.monthTotal ?: 0
                    _slices.value = _analysisData.value?.category?.map { it.value } ?: emptyList()
                    _items.value = _analysisData.value?.category?.filter { it.value != 0 }?.map { CategoryItemRaw(it.key, it.value) } ?: emptyList()
                    _line1Data.value = _analysisData.value?.thisMonthAccumulation?.map { it.value.toFloat() } ?: emptyList()
                    _line2Data.value = _analysisData.value?.lastMonthAccumulation?.map { it.value.toFloat() } ?: emptyList()
                } else {
                    // Handle API failure
                    _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                // Handle network errors or other exceptions
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }
}
