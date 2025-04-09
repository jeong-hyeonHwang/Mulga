package com.ilm.mulga.feature.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme
@Composable
fun TransactionBatchPanel(
    onMergeClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onCancelClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                color = MulGaTheme.colors.grey1.copy(alpha = 0.9f),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(vertical = 8.dp, horizontal = 40.dp)
    ) {
        // Row로 내부 아이템(합치기, 삭제, 취소)을 가로 방향으로 배치
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1) 합치기
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onMergeClick() },
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_util_intersect),
                    contentDescription = stringResource(id = R.string.btn_title_merge),
                    tint = MulGaTheme.colors.white1
                )
                Text(
                    text = stringResource(id = R.string.btn_title_merge),
                    style = MulGaTheme.typography.caption,
                    color = MulGaTheme.colors.white1
                )
            }

            // 2) 삭제
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onDeleteClick() }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_util_trash),
                    contentDescription = stringResource(id = R.string.btn_title_delete),
                    tint = MulGaTheme.colors.white1
                )
                Text(
                    text = stringResource(id = R.string.btn_title_delete),
                    style = MulGaTheme.typography.caption,
                    color = MulGaTheme.colors.white1
                )
            }

            // 3) 취소
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCancelClick() }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_util_x),
                    contentDescription = stringResource(id = R.string.btn_title_withdrawal),
                    tint = MulGaTheme.colors.white1
                )
                Text(
                    text = stringResource(id = R.string.btn_title_cancel),
                    style = MulGaTheme.typography.caption,
                    color = MulGaTheme.colors.white1
                )
            }
        }
    }
}

@Preview
@Composable
fun TransactionBatchPanelPreview() {
    TransactionBatchPanel()
}
