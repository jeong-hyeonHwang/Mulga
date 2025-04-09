package com.ilm.mulga.feature.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.analysis.components.CategoryList
import com.ilm.mulga.feature.analysis.components.DonutChart
import com.ilm.mulga.feature.analysis.components.Graphs
import com.ilm.mulga.feature.analysis.components.PaymentList
import com.ilm.mulga.feature.analysis.components.Separator
import com.ilm.mulga.feature.analysis.components.YearMonthSelector
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.ilm.mulga.feature.analysis.components.PaymentItemData

@Composable
fun AnalysisScreen(analysisNavController: NavController, viewModel: AnalysisViewModel) {

    // Get today's date for initializing
    val selectedYear = viewModel.selectedYear.value
    val selectedMonth = viewModel.selectedMonth.value

    // State to hold the API response data
    val analysisData by viewModel.analysisData
    val loading by viewModel.loading
    val errorMessage by viewModel.errorMessage

    // Call the API whenever the year or month changes
    LaunchedEffect(selectedYear, selectedMonth) {
        viewModel.fetchAnalysisData(selectedYear, selectedMonth)
    }

    // Example list of items passed to CategoryList
    val categoryItems = viewModel.items.value

    val total = viewModel.total.value

    val detail = categoryItems.size <= 5

    val paymentItems = analysisData?.paymentMethod
        ?.map { (source, amount) ->
            PaymentItemData(source, amount)
        } ?: emptyList()

    val chartSlices = viewModel.slices.value

    val chartSlicesSimplified = if (chartSlices.size > 5) {
        chartSlices.take(5) + listOf(total - chartSlices.take(5).sum())
    } else {
        chartSlices
    }

    val monthTotals = analysisData?.monthlyTrend?.map { it.monthTotal.toFloat() } ?: emptyList()

    val safeMonthTotals = monthTotals.ifEmpty { List(6) { 0f } }

    val line1Data = viewModel.line1Data.value
    val line2Data = viewModel.line2Data.value

    // Ensure that line1Data and line2Data are not empty before passing to Graphs
    val safeLine1Data = line1Data.ifEmpty { List(31) { 0f } }
    val safeLine2Data = line2Data.ifEmpty { List(31) { 0f } }

    // Handle loading, error, or success state in the UI
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        YearMonthSelector(
            detail = false,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            onYearMonthChanged = { year, month ->
                viewModel.setSelectedYear(year)
                viewModel.setSelectedMonth(month)
            }
        )

        if (loading) {
            // Show loading indicator
        }

        errorMessage?.let {
            // Show error message
        }

        DonutChart(
            slices = chartSlicesSimplified,
            total = total,
            modifier = Modifier.size(300.dp)
        )

        CategoryList(items = categoryItems.take(5), total = total, detail = detail, analysisNavController = analysisNavController)

        Separator()

        PaymentList(items = paymentItems)

        Separator()

        Graphs(safeMonthTotals, safeLine1Data, safeLine2Data, selectedYear, selectedMonth)
    }
}
