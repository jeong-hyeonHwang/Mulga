package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mulga.ui.theme.MulGaTheme

// PaymentList Composable to display the list of items without using LazyColumn
@Composable
fun PaymentList(items: List<PaymentItemData>) {
    // Define a fixed title here
    val title = "지출 수단별 이용내역"  // Fixed title

    Column(modifier = Modifier.padding(28.dp)) {
        // Title at the top
        Text(
            text = title,  // The fixed title
            style = MulGaTheme.typography.bodyLarge,  // Style for the title
            modifier = Modifier.padding(bottom = 16.dp)  // Padding below the title
        )

        items.forEach { item ->
            PaymentItem(
                source = item.source,
                amount = item.amount
            )
        }
    }
}
