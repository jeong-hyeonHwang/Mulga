package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

// CategoryList Composable to display the list of items
@Composable
fun CategoryList(items: List<CategoryItemData>) {
    LazyColumn {
        items(items) { item ->
            CategoryItem(
                circleColor = item.circleColor,
                firstText = item.firstText,
                secondText = item.secondText,
                rightText = item.rightText
            )
        }
    }
}