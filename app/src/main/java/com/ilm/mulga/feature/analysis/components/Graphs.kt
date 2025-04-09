package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Graphs(
    barHeights: List<Float>,
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
            currentMonth = month
        )
        Spacer(modifier = Modifier.height(16.dp)) // Add some space between graphs

        // Line Graph
        MonthlyLineGraph(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            year = year,
            month = month,
            prevMonthData = lineData2,
            currMonthData = lineData1
        )
    }
}
