package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import com.example.mulga.ui.theme.MulGaTheme
import com.example.mulga.R // Make sure to import your drawable resources

data class PaymentItemData(
    val source: String,
    val amount: String
)

// Function to map firstText to corresponding icon
fun getIconResource(source: String): Int {
    return when (source) {
        "신한은행" -> R.drawable.ic_pay_shinhanbank
        "네이버페이" -> R.drawable.ic_pay_naverpay
        "국민은행" -> R.drawable.ic_pay_kbbank
        "카카오뱅크" -> R.drawable.ic_pay_kakaobank
        "카카오페이" -> R.drawable.ic_pay_kakaopay
        else -> R.drawable.ic_pay_naverpay // Fallback icon if no match
    }
}

// PaymentItem Composable for individual items
@Composable
fun PaymentItem(
    source: String = "기타", // Example, you can pass this dynamically
    amount: String = "0"
) {
    val icon = getIconResource(source) // Get the icon resource dynamically based on firstText

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon from drawable resource
        Box(
            modifier = Modifier
                .size(36.dp) // Size of the box
                .clip(RoundedCornerShape(4.dp)) // Curved square shape
        ) {
            // Image from drawable resource filling the box
            Image(
                painter = painterResource(id = icon), // Load the image from resources
                contentDescription = null, // Optional: content description for accessibility
                modifier = Modifier.fillMaxSize() // Makes the image fill the entire box
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between icon and text boxes

        // Column for the first vertical text box
        Column(
            modifier = Modifier.weight(1f) // Makes this column take available space
        ) {
            Text(source, style = MulGaTheme.typography.bodySmall) // First line text, customizable
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between text column and the far-right text box

        // Text box on the far right with customizable text
        Text(amount, style = MulGaTheme.typography.bodySmall) // Right text, customizable
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentItem() {
    // You can test it with different firstText values
    PaymentItem(source = "네이버페이", amount = "200,500")
}
