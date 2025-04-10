package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.draw.alpha
import com.ilm.mulga.ui.theme.MulGaTheme
import java.time.LocalDate

@Composable
fun YearMonthSelector(
    detail: Boolean,
    selectedYear: Int,
    selectedMonth: Int,
    onYearMonthChanged: (Int, Int) -> Unit // Callback to update year and month
) {
    val currentDate = LocalDate.now()
    val currentYear = currentDate.year
    val currentMonth = currentDate.monthValue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Chevron button (for previous month)
        IconButton(onClick = {
            if (selectedMonth == 1) {
                onYearMonthChanged(selectedYear - 1, 12)
            } else {
                onYearMonthChanged(selectedYear, selectedMonth - 1)
            }
        },
            enabled = !detail,
            modifier = Modifier.alpha(if (detail) 0f else 1f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, // Left chevron icon
                contentDescription = "Previous Month",
                modifier = Modifier.size(32.dp) // Size of the chevron icon
            )
        }

        // Year and Month Text
        Text(
            text = "${selectedYear}년 ${selectedMonth}월",
            style = MulGaTheme.typography.title,
            color = MulGaTheme.colors.grey1,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

        // Right Chevron button (for next month)
        IconButton(
            onClick = {
                if (selectedMonth == 12) {
                    onYearMonthChanged(selectedYear + 1, 1)
                } else {
                    onYearMonthChanged(selectedYear, selectedMonth + 1)
                }
            },
            enabled = selectedYear < currentYear || (selectedYear == currentYear && selectedMonth < currentMonth) || detail, // Disable if current month
            modifier = Modifier.alpha(if ((selectedYear == currentYear && selectedMonth == currentMonth) || detail) 0f else 1f) // Make chevron invisible if current month
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // Right chevron icon
                contentDescription = "Next Month",
                modifier = Modifier.size(32.dp) // Size of the chevron icon
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YearMonthSelectorPreview() {
    YearMonthSelector(
        detail = false,
        selectedYear = 2025,
        selectedMonth = 4,
        onYearMonthChanged = { _, _ -> }
    )
}
