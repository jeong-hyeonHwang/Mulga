package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Graphs(
    barHeights: List<Float>,
    barLabels: List<String>,
    lineData1: List<Float>,
    lineData2: List<Float>,
    year: Int,
    month: Int
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Bar Graph
        MonthlyBarGraph(
            modifier = Modifier.fillMaxWidth(),
            amount = barHeights,
            labelTexts = barLabels
        )
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between graphs

        // Line Graph
        MonthlyLineGraph(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            year = year,
            month = month,
            prevMonthData = lineData1,
            currMonthData = lineData2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GraphSectionPreview() {
    Graphs(
        barHeights = listOf(100f, 120f, 80f, 150f, 90f, 110f),
        barLabels = listOf("10월", "11월", "12월", "1월", "2월", "3월"),
        lineData1 = listOf(0f, 0f, 20f, 20f, 20f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 200f, 200f, 200f, 200f, 200f, 200f),
        lineData2 = listOf(0f, 10f, 10f, 10f, 10f, 20f, 20f, 20f, 20f, 40f, 50f, 50f, 50f, 50f, 50f, 70f, 70f, 80f, 100f, 120f, 120f, 150f, 150f, 150f, 190f, 210f, 240f, 250f, 250f, 250f, 250f),
        year = 2025,
        month = 4
    )
}
