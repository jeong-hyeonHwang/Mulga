package com.ilm.mulga.feature.calendar.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun TransactionDayHeader(dateLabel: String) {
    Text(
        text = dateLabel,
        style = MulGaTheme.typography.caption,
        color = MulGaTheme.colors.grey1,
    )
}
