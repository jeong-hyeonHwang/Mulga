package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.R
import com.ilm.mulga.util.extension.withCommas

@Composable
fun TransactionHeaderView(
    category: Category,
    cost: Int,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = category.displayName,
                color = MulGaTheme.colors.grey1,
                style = MulGaTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.budget_value_unit, cost.toString().withCommas()),
                style = MulGaTheme.typography.headline
            )

            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_util_pencil),
                    contentDescription = "Edit",
                    tint = MulGaTheme.colors.grey2
                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionHeaderViewPreview() {
    TransactionHeaderView(
        category = Category.CAFE,
        cost = 50000,
        onEditClick = {}
    )
}