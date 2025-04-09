package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import kotlin.math.ceil
import androidx.compose.ui.draw.blur
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.ilm.mulga.ui.theme.MulGaTheme

// CategoryList Composable to display the list of items (with a fallback and blur when null)
@Composable
fun CategoryList(items: List<CategoryItemRaw>?, total: Int, detail: Boolean, analysisNavController: NavController? = null) {
    // Fallback list of 4 empty CategoryItemRaw objects when items is null
    val fallbackItems = listOf(
        CategoryItemRaw(category = "food", amount = 312400),
        CategoryItemRaw(category = "transfer", amount = 115250),
        CategoryItemRaw(category = "cafe", amount = 43500),
        CategoryItemRaw(category = "home", amount = 13422)
    )

    // Use fallback items if the list is null
    val displayItems = items?.ifEmpty { fallbackItems }

    // Apply blur effect if the list is empty
    val modifier = if (items.isNullOrEmpty()) {
        Modifier.blur(4.dp).alpha(0.5f)
    } else {
        Modifier
    }

    Box(
        modifier = Modifier.padding(28.dp)
    ) {
        // Column to display the items
        Column(modifier = modifier) {
            // Iterate over the items and display each CategoryItem
            displayItems?.forEach { item ->
                CategoryItem(
                    category = item.category,
                    portion = (ceil(item.amount * 100.0 / total).toInt()).toString() + "%",
                    amount = NumberFormat.getNumberInstance().format(item.amount)
                )
            }

            // Optionally, display "View More" button or text at the bottom
            if (!detail) {
                // Check if analysisNavController is null
                analysisNavController?.let {
                    ViewMore(analysisNavController = it) // Use the provided NavController
                } ?: run {
                    // Handle case when NavController is null, if needed
                }
            }
        }

        // If the list is empty, display the "No data" text
        if (items.isNullOrEmpty()) {
            Text(
                text = "결제 내역이 있으면\n분류가 표시됩니다",
                modifier = Modifier.align(Alignment.Center),
                style = MulGaTheme.typography.title, // Customize the color
                color = MulGaTheme.colors.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}
