package com.ilm.mulga.feature.calendar.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.R
import com.ilm.mulga.presentation.model.TransactionItemData
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import com.ilm.mulga.util.extension.withCommas

@Composable
fun TransactionItem(
    item: TransactionItemData
) {
    // category가 null이면 아이콘 영역은 LightGray 색상, 있으면 배경은 Transparent
    val iconBackgroundColor = if (item.category == null) MulGaTheme.colors.grey2 else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 아이콘 영역
        // category가 있으면 해당 아이콘 이미지, 없으면 회색 원만 표시
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    color = iconBackgroundColor,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (item.category != null) {
                Image(
                    painter = painterResource(id = item.category.iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // 텍스트 영역 (제목, 부제목)
        Column(
            modifier = Modifier
                .weight(0.65f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = item.title,
                color = MulGaTheme.colors.black1,
                style = MulGaTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.subtitle,
                color = MulGaTheme.colors.grey2,
                style = MulGaTheme.typography.caption
            )
        }

        // 오른쪽: 가격, 시간 영역
        Column(
            modifier = Modifier.weight(0.25f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            Text(
                stringResource(id = R.string.budget_value_unit, item.price.withCommas()),
                color = MulGaTheme.colors.black1,
                style = MulGaTheme.typography.bodySmall
            )
            Text(
                text = item.time,
                color = MulGaTheme.colors.grey2,
                style = MulGaTheme.typography.caption
            )
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    // Preview에서 category가 없는 경우와 있는 경우를 테스트합니다.
    Column {
        TransactionItem(
            TransactionItemData(
                category = null,
                title = "무엇인지 잘 몰라요잉",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-2455500",
                time = "08:23"
            )
        )

        TransactionItem(
            TransactionItemData(
                category = Category.CAFE,
                title = "아이스 아메리카노",
                subtitle = "LGU+ 카드의 정식 | 메가커피",
                price = "-1500",
                time = "08:23"
            )
        )
    }
}
