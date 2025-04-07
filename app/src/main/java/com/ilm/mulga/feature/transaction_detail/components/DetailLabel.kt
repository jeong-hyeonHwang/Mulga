package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun DetailLabel(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.width(108.dp),
            text = label,
            color = MulGaTheme.colors.grey2,
            style = MulGaTheme.typography.bodyLarge
        )
        Text(
            text = value,
            color = MulGaTheme.colors.black1,
            style = MulGaTheme.typography.bodyLarge
        )
    }
}
