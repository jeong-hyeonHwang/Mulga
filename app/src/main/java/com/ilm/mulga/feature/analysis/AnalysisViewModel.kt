package com.ilm.mulga.feature.analysis

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import com.ilm.mulga.feature.analysis.components.CategoryItemRaw
import java.time.LocalDate

class AnalysisViewModel : ViewModel() {
    // You can define the data to be shared between screens
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

    // Functions to update the data
    fun setTotal(value: Int) {
        _total.intValue = value
    }

    fun setSlices(value: List<Int>) {
        _slices.value = value
    }

    fun setItems(value: List<CategoryItemRaw>) {
        _items.value = value
    }

    // Function to update line1Data
    fun setLine1Data(value: List<Float>) {
        _line1Data.value = value
    }

    // Function to update line2Data
    fun setLine2Data(value: List<Float>) {
        _line2Data.value = value
    }

    // Function to set selected year
    fun setSelectedYear(year: Int) {
        _selectedYear.intValue = year
    }

    // Function to set selected month
    fun setSelectedMonth(month: Int) {
        _selectedMonth.intValue = month
    }
}
