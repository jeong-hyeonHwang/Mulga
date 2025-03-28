package com.example.mulga.feature.analysis


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mulga.feature.analysis.components.DonutChart

@Preview
@Composable
fun AnalysisScreen() {
    Text(text = "분석 화면")
    DonutChart()
}
