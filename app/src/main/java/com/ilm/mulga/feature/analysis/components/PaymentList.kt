package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

// PaymentList Composable to display the list of items without using LazyColumn
@Composable
fun PaymentList(items: List<PaymentItemData>) {

    val fallbackItems = listOf(
        PaymentItemData(source = "신한은행", amount = 324800),
        PaymentItemData(source = "네이버페이", amount = 242350),
        PaymentItemData(source = "국민은행", amount = 121300),
        PaymentItemData(source = "카카오페이", amount = 55000)
    )

    // Use fallback items if the list is null
    val displayItems = items.ifEmpty { fallbackItems }

    // Define a fixed title here
    val title = "지출 수단별 이용내역"  // Fixed title

    val modifier = if (items.isNullOrEmpty()) {
        Modifier.blur(4.dp).alpha(0.5f)
    } else {
        Modifier
    }

    Box(
        modifier = Modifier.padding(28.dp)
    ) {
        Column(modifier = modifier) {
            // Title at the top
            Text(
                text = title,  // The fixed title
                style = MulGaTheme.typography.bodyLarge,  // Style for the title
                modifier = Modifier.padding(bottom = 16.dp)  // Padding below the title
            )

            displayItems.forEach { item ->
                PaymentItem(
                    source = item.source,
                    amount = item.amount
                )
            }
        }

        if (items.isEmpty()) {
            Text(
                text = "결제 내역이 있으면\n지출 수단이 표시됩니다",
                modifier = Modifier.align(Alignment.Center),
                style = MulGaTheme.typography.title, // Customize the color
                color = MulGaTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
