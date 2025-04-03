package com.example.mulga.feature.analysis


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mulga.feature.analysis.components.CategoryItemData
import com.example.mulga.feature.analysis.components.CategoryList
import com.example.mulga.feature.analysis.components.DonutChart
import com.example.mulga.ui.theme.MulGaTheme

@Preview(showBackground = true)
@Composable
fun AnalysisScreen() {
    Text(text = "분석 화면")
    DonutChart()
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryItem() {
    // Example list of items passed to CategoryList
    val itemList = listOf(
        CategoryItemData(
            circleColor = MulGaTheme.colors.categoryShopping.copy(alpha = 0.1f),
            firstText = "쇼핑",
            secondText = "50%",
            rightText = "501,250"
        ),
        CategoryItemData(
            circleColor = MulGaTheme.colors.categoryFood.copy(alpha = 0.1f),
            firstText = "식사",
            secondText = "30%",
            rightText = "200,000"
        ),
        CategoryItemData(
            circleColor = MulGaTheme.colors.categoryTravel.copy(alpha = 0.1f),
            firstText = "여행",
            secondText = "20%",
            rightText = "100,000"
        )
    )

    CategoryList(items = itemList)
}
