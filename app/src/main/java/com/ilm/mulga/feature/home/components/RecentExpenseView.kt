package com.ilm.mulga.feature.home.components

import TransactionItem
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun RecentExpenseView(
    item: TransactionItemData?,
    modifier: Modifier
) {
    val contentHeight = 42.dp

    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Text(
            text = stringResource(id = R.string.main_recent_expense),
            style = MulGaTheme.typography.subtitle,
            color = MulGaTheme.colors.black1
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight),
        ) {
            if (item != null) {
                TransactionItem(
                    item = item,
                    onClick = { },
                    onLongPress = { },
                    isSelected = false,
                    isDeleteMode = false,
                    isCombined = item.isCombined
                )
            } else {
                Text(
                    text = stringResource(R.string.main_no_recent_expense),
                    style = MulGaTheme.typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Preview
@Composable
fun RecentExpenseViewPreview() {
    val item: TransactionItemData = TransactionItemData(
        id = "sad....",
        category = Category.FOOD,
        title = "냠",
        subtitle = "어딘가 어떤 카드로",
        price = "50000",
        time = "some",
    )
    RecentExpenseView(item, Modifier.padding(32.dp))
}