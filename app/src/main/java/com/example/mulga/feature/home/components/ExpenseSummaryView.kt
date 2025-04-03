package com.example.mulga.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.ui.theme.MulGaTheme
import com.example.mulga.util.extension.withCommas

@Composable
fun ExpenseSummaryView(
    monthTotal: String,
    remainingBudget: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.main_expense_this_month),
            style = MulGaTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.budget_value_unit, monthTotal.withCommas()),
            style = MulGaTheme.typography.display,
            textAlign = TextAlign.Center,
            modifier = Modifier.height(48.dp)
        )
        Text(
            text = stringResource(R.string.main_remaining_budget, remainingBudget.withCommas()),
            style = MulGaTheme.typography.caption,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun ExpenseSummaryViewPreview() {
    ExpenseSummaryView(
        monthTotal = "100000",
        remainingBudget = "10000000",
        Modifier.fillMaxWidth())
}