package com.example.mulga.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.feature.calendar.components.TransactionItem
import com.example.mulga.presentation.model.TransactionItemData
import com.example.mulga.presentation.model.type.Category
import com.example.mulga.ui.theme.MulGaTheme

@Composable
fun RecentExpenseView(
    item: TransactionItemData,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(text = stringResource(id = R.string.main_recent_expense),
             style = MulGaTheme.typography.subtitle,
            color = MulGaTheme.colors.black1)
        TransactionItem(item = item)
    }
}

@Preview
@Composable
fun RecentExpenseViewPreview() {
    val item: TransactionItemData = TransactionItemData(
        category = Category.FOOD,
        title = "냠",
        subtitle = "어딘가 어떤 카드로",
        price = "50000",
        time = "some"
    )
    RecentExpenseView(item, Modifier.padding(32.dp))
}