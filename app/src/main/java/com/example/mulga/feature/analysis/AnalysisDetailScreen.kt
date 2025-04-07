package com.example.mulga.feature.analysis

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
import com.example.mulga.feature.analysis.components.CategoryItemData
import com.example.mulga.feature.analysis.components.CategoryList
import com.example.mulga.feature.analysis.components.DonutChart
import com.example.mulga.feature.analysis.components.DonutSlice
import com.example.mulga.feature.analysis.components.Graphs
import com.example.mulga.feature.analysis.components.PaymentItemData
import com.example.mulga.feature.analysis.components.PaymentList
import com.example.mulga.feature.analysis.components.Separator
import com.example.mulga.feature.analysis.components.YearMonthSelector

@Preview(showBackground = true)
@Composable
fun AnalysisDetailScreen() {
    // Example list of items passed to CategoryList
    val itemList = listOf(
        CategoryItemData(
            category = "shopping",
            secondText = "50%",
            rightText = "501,250"
        ),
        CategoryItemData(
            category = "food",
            secondText = "30%",
            rightText = "200,000"
        ),
        CategoryItemData(
            category = "travel",
            secondText = "20%",
            rightText = "100,000"
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

    val barHeights = listOf(100f, 120f, 80f, 150f, 90f, 110f) // Custom heights for each bar
    val labelTexts = listOf("10월", "11월", "12월", "1월", "2월", "3월") // Custom labels
    val year = 2025
    val month = 4 // April
    val line1Data = listOf(0f, 0f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 200f, 200f, 200f, 200f, 200f, 200f) // Data for the first line
    val line2Data = listOf(0f, 10f, 10f, 10f, 10f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 210f, 240f, 250f, 250f, 250f, 250f) // Data for the second line

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),  // Make sure it takes up the full screen or container space
        horizontalAlignment = Alignment.CenterHorizontally,  // Center the content horizontally
    ) {
        YearMonthSelector(detail = true)

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
