package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import com.example.mulga.ui.theme.MulGaTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Icon

data class PaymentItemData(
    val iconColor: Color,
    val firstText: String,
    val rightText: String
)

// PaymentItem Composable for individual items
@Composable
fun PaymentItem(
    iconColor: Color = MulGaTheme.colors.categoryCafe.copy(alpha = 0.1f),
    firstText: String = "쇼핑",
    rightText: String = "501,250"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder Icon inside a curved square
        Box(
            modifier = Modifier
                .size(36.dp) // Adjust size as necessary
                .clip(RoundedCornerShape(4.dp)) // Curved square
                .background(iconColor) // Set background color
                .padding(10.dp) // Padding for icon
        ) {
            // Placeholder Icon (currently Info icon, you can change this)
            Icon(
                imageVector = Icons.Default.Info, // You can replace with your desired icon
                contentDescription = null, // Optional: content description for accessibility
                modifier = Modifier.fillMaxSize() // Icon takes up full size of the box
            )
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between icon and text boxes

        // Column for the first vertical text box
        Column(
            modifier = Modifier.weight(1f) // Makes this column take available space
        ) {
            Text(firstText, style = MulGaTheme.typography.bodySmall) // First line text, customizable
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between text column and the far-right text box

        // Text box on the far right with customizable text
        Text(rightText, style = MulGaTheme.typography.bodySmall) // Right text, customizable
    }
}
