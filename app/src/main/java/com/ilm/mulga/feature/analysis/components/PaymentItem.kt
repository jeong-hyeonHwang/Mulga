package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.R
import java.text.NumberFormat

data class PaymentItemData(
    val source: String,
    val amount: Int
)

// Function to map firstText to corresponding icon or fallback
fun getIconResource(source: String): @Composable () -> Unit {
    return when (source) {
        "신한은행" -> { { Image(painter = painterResource(id = R.drawable.ic_pay_shinhanbank), contentDescription = null, modifier = Modifier.fillMaxSize()) } }
        "네이버페이" -> { { Image(painter = painterResource(id = R.drawable.ic_pay_naverpay), contentDescription = null, modifier = Modifier.fillMaxSize()) } }
        "국민은행" -> { { Image(painter = painterResource(id = R.drawable.ic_pay_kbbank), contentDescription = null, modifier = Modifier.fillMaxSize()) } }
        "카카오뱅크" -> { { Image(painter = painterResource(id = R.drawable.ic_pay_kakaobank), contentDescription = null, modifier = Modifier.fillMaxSize()) } }
        "카카오페이" -> { { Image(painter = painterResource(id = R.drawable.ic_pay_kakaopay), contentDescription = null, modifier = Modifier.fillMaxSize()) } }
        else -> {
            // Fallback: Grey box with the first letter of source
            {
                Box(
                    modifier = Modifier
                        .size(36.dp) // Size of the box
                        .clip(RoundedCornerShape(4.dp))
                        .background(MulGaTheme.colors.grey2) // Grey background color
                ) {
                    Text(
                        text = source.first().toString(), // Display the first letter of the source
                        modifier = Modifier.align(Alignment.Center),
                        style = MulGaTheme.typography.bodyLarge.copy(color = MulGaTheme.colors.white1) // White text on the grey box
                    )
                }
            }
        }
    }
}

// PaymentItem Composable for individual items
@Composable
fun PaymentItem(
    source: String = "기타", // Example, you can pass this dynamically
    amount: Int = 0
) {
    val icon = getIconResource(source) // Get the icon resource dynamically based on the source

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon from drawable resource or fallback icon
        Box(
            modifier = Modifier
                .size(36.dp) // Size of the box
                .clip(RoundedCornerShape(4.dp)) // Curved square shape
        ) {
            icon() // Use the composable function to render the icon or fallback
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
        Text(NumberFormat.getNumberInstance().format(amount), style = MulGaTheme.typography.bodySmall) // Right text, customizable
    }
}
