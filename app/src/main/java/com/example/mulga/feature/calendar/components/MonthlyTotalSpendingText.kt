package com.example.mulga.feature.calendar.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.mulga.R
import com.example.mulga.ui.theme.MulGaTheme
import com.example.mulga.util.extension.withCommas

@Composable
fun MonthlyTotalSpendingText(
    amount: String
) {
    Text(
        text =  stringResource(id = R.string.budget_value_unit, amount.withCommas()),
        textAlign = TextAlign.Start,
        style = MulGaTheme.typography.headline,
        color = MulGaTheme.colors.grey1
    )
}

@Preview
@Composable
fun MonthlyTotalSpendingTextPreview () {
    MonthlyTotalSpendingText("11111111111111")
}
