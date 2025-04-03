package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// CategoryList Composable to display the list of items
@Composable
fun CategoryList(items: List<CategoryItemData>) {
    Column(modifier = Modifier.padding(28.dp)) {
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
}