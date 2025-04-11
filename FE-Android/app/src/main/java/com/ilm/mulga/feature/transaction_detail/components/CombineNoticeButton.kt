package com.ilm.mulga.feature.transaction_detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable
fun CombineNoticeButton(
    combineNum: String = "2",
    onClick: () -> Unit = {}
){
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(26.dp),
        shape = RoundedCornerShape(100),  // 예: RoundedCornerShape(50) 등으로 바꿔서 pill 모양을 만들 수 있음
        border = BorderStroke(1.dp, MulGaTheme.colors.primary), // 테두리 색상
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MulGaTheme.colors.transparent,      // 버튼 내부 색상
            contentColor = MulGaTheme.colors.primary    // 텍스트, 아이콘 색상
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = combineNum + "개 합쳤어요",
                style = MulGaTheme.typography.caption
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_util_caret_right),
                contentDescription = "Arrow",
                tint = MulGaTheme.colors.primary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCombineNoticeButton() {
    CombineNoticeButton()
}