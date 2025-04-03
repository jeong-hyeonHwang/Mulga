package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import com.example.mulga.ui.theme.MulGaTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip

data class CategoryItemData(
    val circleColor: Color,
    val firstText: String,
    val secondText: String,
    val rightText: String
)

// CategoryItem Composable for individual items
@Composable
fun CategoryItem(
    circleColor: Color = MulGaTheme.colors.categoryCafe.copy(alpha = 0.1f),
    firstText: String = "쇼핑",
    secondText: String = "50%",
    rightText: String = "501,250"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 6.dp, start = 30.dp, end = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circle on the left with customizable color
        Box(
            modifier = Modifier
                .size(40.dp) // Adjust size as necessary
                .clip(CircleShape)
                .background(circleColor) // Set circle color from parameter
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between circle and text boxes

        // Column for the two vertical text boxes
        Column(
            modifier = Modifier.weight(1f) // Makes this column take available space
        ) {
            Text(firstText, style = MulGaTheme.typography.bodySmall) // First line text, customizable
            Text(secondText, style = MulGaTheme.typography.caption, color = MulGaTheme.colors.grey1) // Second line text, customizable
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between text column and the far-right text box

        // Text box on the far right with customizable text
        Text(rightText, style = MulGaTheme.typography.bodySmall) // Right text, customizable
    }
}
