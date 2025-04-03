package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mulga.ui.theme.MulGaTheme

// CategoryList Composable to display the list of items
@Composable
fun PaymentList(items: List<PaymentItemData>) {
    // Define a fixed title here
    val title = "지출 수단별 이용내역"  // Fixed title

    Column(modifier = Modifier.padding(28.dp)) {
        // Title at the top
        Text(
            text = title,  // The fixed title
            style = MulGaTheme.typography.bodyLarge,  // Style for the title
            modifier = Modifier.padding(bottom = 2.dp)  // Padding below the title
        )

        // LazyColumn to display the list of payment items
        LazyColumn {
            items(items) { item ->
                PaymentItem(
                    iconColor = item.iconColor,
                    firstText = item.firstText,
                    rightText = item.rightText
                )
            }
        }
    }
}