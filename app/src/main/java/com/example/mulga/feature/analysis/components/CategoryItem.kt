package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.example.mulga.presentation.model.type.Category
import com.example.mulga.ui.theme.MulGaTheme

data class CategoryItemData(
    val category: String,
    val secondText: String,
    val rightText: String
)

// CategoryItem Composable for individual items
@Composable
fun CategoryItem(
    category: String,
    secondText: String = "50%", // Customize this text as needed
    rightText: String = "501,250" // Customize this text as needed
) {
    val categoryEnum = Category.entries.find { it.backendKey.equals(category, ignoreCase = true) }

    // If the categoryEnum is not found, handle it (for example, show a placeholder or an error)
    if (categoryEnum == null) {
        // Handle null case (e.g., show a default icon or display an error)
        return
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 0.dp, start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon on the left with the image from the Category enum
        Image(
            painter = painterResource(id = categoryEnum.iconResId), // Use the iconResId from the Category enum
            contentDescription = categoryEnum.displayName, // Optional: Provide a description for accessibility
            modifier = Modifier
                .size(36.dp) // Adjust size as necessary
                .clip(CircleShape) // Optional: to clip the icon to a circular shape
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between icon and text boxes

        // Column for the two vertical text boxes
        Column(
            modifier = Modifier.weight(1f) // Makes this column take available space
        ) {
            Text(categoryEnum.displayName, style = MulGaTheme.typography.bodySmall) // Display the displayName from the Category enum
            Text(secondText, style = MulGaTheme.typography.caption, color = MulGaTheme.colors.grey1) // Second line text, customizable
        }

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between text column and the far-right text box

        // Text box on the far right with customizable text
        Text(rightText, style = MulGaTheme.typography.bodySmall) // Right text, customizable
    }
}
