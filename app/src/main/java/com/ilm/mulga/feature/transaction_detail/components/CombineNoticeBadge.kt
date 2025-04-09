package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun CombineNoticeBadge(
    combineNum: String = "2",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(26.dp)
            .border(
                BorderStroke(1.dp, MulGaTheme.colors.primary),
                shape = RoundedCornerShape(100)
            )
            .background(MulGaTheme.colors.transparent, shape = RoundedCornerShape(100))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = combineNum + "개 합쳤어요",
            color = MulGaTheme.colors.primary,
            style = MulGaTheme.typography.caption
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCombineNoticeBadge() {
    CombineNoticeBadge()
}
