package com.example.mulga.feature.analysis


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mulga.feature.analysis.components.CategoryItemData
import com.example.mulga.feature.analysis.components.CategoryList
import com.example.mulga.feature.analysis.components.DonutChart
import com.example.mulga.feature.analysis.components.PaymentItemData
import com.example.mulga.feature.analysis.components.PaymentList
import com.example.mulga.presentation.model.type.Category
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

    CategoryList(items = itemList)
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentItem() {
    // Example list of items passed to PaymentList
    val itemList = listOf(
        PaymentItemData(
            iconColor = MulGaTheme.colors.categoryShopping,
            firstText = "신한은행",
            rightText = "501,250"
        ),
        PaymentItemData(
            iconColor = MulGaTheme.colors.categoryShopping,
            firstText = "네이버페이",
            rightText = "200,000"
        ),
        PaymentItemData(
            iconColor = MulGaTheme.colors.categoryShopping,
            firstText = "카카오뱅크",
            rightText = "100,000"
        )
    )

    PaymentList(items = itemList)
}
