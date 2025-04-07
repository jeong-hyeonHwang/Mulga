package com.ilm.mulga.feature.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.feature.analysis.components.CategoryItemData
import com.ilm.mulga.feature.analysis.components.CategoryList
import com.ilm.mulga.feature.analysis.components.DonutChart
import com.ilm.mulga.feature.analysis.components.PaymentItemData
import com.ilm.mulga.feature.analysis.components.YearMonthSelector
import com.ilm.mulga.feature.analysis.components.DonutSlice

@Preview(showBackground = true)
@Composable
fun AnalysisDetailScreen() {
    // Example list of items passed to CategoryList
    val itemList = listOf(
        CategoryItemData(
            category = "shopping",
            portion = "50%",
            amount = "501,250"
        ),
        CategoryItemData(
            category = "food",
            portion = "30%",
            amount = "200,000"
        ),
        CategoryItemData(
            category = "travel",
            portion = "20%",
            amount = "100,000"
        )
    )

    val itemList2 = listOf(
        PaymentItemData(
            source = "신한은행",
            amount = "501,250"
        ),
        PaymentItemData(
            source = "네이버페이",
            amount = "200,000"
        ),
        PaymentItemData(
            source = "카카오뱅크",
            amount = "100,000"
        )
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),  // Make sure it takes up the full screen or container space
        horizontalAlignment = Alignment.CenterHorizontally,  // Center the content horizontally
    ) {
        YearMonthSelector(detail = true, selectedYear = 2025,
            selectedMonth = 4,
            onYearMonthChanged = { _, _ -> })

        DonutChart(
            slices = listOf(
                DonutSlice(30f),
                DonutSlice(50f),
                DonutSlice(20f),
                DonutSlice(10f),
                DonutSlice(40f),
                DonutSlice(50f),
                DonutSlice(30f)  // More than 6 slices to see color reuse
            ),
            modifier = Modifier.size(300.dp)
        )

        CategoryList(items = itemList, detail = true)
    }

}
