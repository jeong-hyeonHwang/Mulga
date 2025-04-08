package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import kotlin.math.ceil

// CategoryList Composable to display the list of items (without LazyColumn)
@Composable
fun CategoryList(items: List<CategoryItemRaw>, total: Int, detail: Boolean) {
    Column(modifier = Modifier.padding(28.dp)) {
        // Iterate over the items and display each CategoryItem
        items.forEach { item ->
            CategoryItem(
                category = item.category,
                portion = (ceil(item.amount * 100.0 / total).toInt()).toString() + "%",
                amount = NumberFormat.getNumberInstance().format(item.amount)
            )
        }
        // Optionally, display "View More" button or text at the bottom
        if (!detail) {
            ViewMore()
        }
    }
}
