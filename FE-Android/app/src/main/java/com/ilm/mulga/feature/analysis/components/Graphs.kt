package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun Graphs(
    barHeights: List<Float>,
    lineData1: List<Float>,
    lineData2: List<Float>,
    year: Int,
    month: Int
) {

    val title = "월별 추이"

    Box(
        modifier = Modifier.padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = title,  // The fixed title
                style = MulGaTheme.typography.bodyLarge,  // Style for the title
                modifier = Modifier.padding(bottom = 16.dp)  // Padding below the title
            )

            // Bar Graph
            MonthlyBarGraph(
                modifier = Modifier.fillMaxWidth(),
                amount = barHeights,
                currentMonth = month
            )

            Spacer(modifier = Modifier.height(16.dp))

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
}
