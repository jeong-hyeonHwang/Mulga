package com.ilm.mulga.feature.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ilm.mulga.feature.analysis.components.CategoryList
import com.ilm.mulga.feature.analysis.components.DonutChart
import com.ilm.mulga.feature.analysis.components.YearMonthSelector

@Composable
fun AnalysisDetailScreen(viewModel: AnalysisViewModel) {

    val selectedYear = viewModel.selectedYear.value

    val selectedMonth = viewModel.selectedMonth.value

    val itemList = viewModel.items.value

    val total = viewModel.total.value

    val slices = viewModel.slices.value

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),  // Make sure it takes up the full screen or container space
        horizontalAlignment = Alignment.CenterHorizontally,  // Center the content horizontally
    ) {
        YearMonthSelector(detail = true, selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            onYearMonthChanged = { _, _ -> })

        DonutChart(
            slices = slices,
            total = total,
            modifier = Modifier.size(300.dp)
        )

        CategoryList(items = itemList, total = total, detail = true)
    }

}
