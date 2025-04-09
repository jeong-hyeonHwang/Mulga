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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ilm.mulga.feature.analysis.components.CategoryList
import com.ilm.mulga.feature.analysis.components.DonutChart
import com.ilm.mulga.feature.analysis.components.YearMonthSelector

//@Preview(showBackground = true)
@Composable
fun AnalysisDetailScreen() {

    // Example list of items passed to CategoryList
    val viewModel: AnalysisViewModel = viewModel()

    val itemList = viewModel.items.value

    val total = viewModel.total.value

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),  // Make sure it takes up the full screen or container space
        horizontalAlignment = Alignment.CenterHorizontally,  // Center the content horizontally
    ) {
        YearMonthSelector(detail = true, selectedYear = 2025,
            selectedMonth = 4,
            onYearMonthChanged = { _, _ -> })

        DonutChart(
            slices = listOf(30, 50, 20, 10, 40, 50, 30),
            total = 200,
            modifier = Modifier.size(300.dp)
        )

        CategoryList(items = itemList, total = total, detail = true)
    }

}
