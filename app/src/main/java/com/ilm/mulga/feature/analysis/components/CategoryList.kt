package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// CategoryList Composable to display the list of items (without LazyColumn)
@Composable
fun CategoryList(items: List<CategoryItemData>, detail: Boolean) {
    Column(modifier = Modifier.padding(28.dp)) {
        // Iterate over the items and display each CategoryItem
        items.forEach { item ->
            CategoryItem(
                category = item.category,
                portion = item.portion,
                amount = item.amount
            )
        }
        // Optionally, display "View More" button or text at the bottom
        if (!detail) {
            ViewMore()
        }
    }
}
// Preview for CategoryList (without LazyColumn)
@Preview(showBackground = true)
@Composable
fun PreviewCategoryList() {
    // Example list of CategoryItemData for the preview
    val sampleItems = listOf(
        CategoryItemData(category = "Shopping", portion = "50%", amount = "501,250"),
        CategoryItemData(category = "Food", portion = "30%", amount = "200,000"),
        CategoryItemData(category = "Travel", portion = "20%", amount = "100,000"),
        CategoryItemData(category = "Travel", portion = "20%", amount = "100,000"),
        CategoryItemData(category = "Travel", portion = "20%", amount = "100,000")
    )

    // Display the CategoryList with the sample items
    CategoryList(items = sampleItems, detail = false)
}
