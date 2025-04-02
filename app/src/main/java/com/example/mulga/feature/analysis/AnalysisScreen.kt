package com.ilm.mulga.feature.analysis


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ilm.mulga.feature.analysis.components.DonutChart

@Preview
@Composable
fun AnalysisScreen() {
    Text(text = "분석 화면")
    DonutChart()
}
