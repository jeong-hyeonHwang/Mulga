package com.example.mulga.feature.calendar.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.R
import com.example.mulga.ui.theme.LocalMulGaColors
import com.example.mulga.ui.theme.LocalMulGaTypography
import java.util.Calendar

@SuppressLint("RememberReturnType")
@Composable
fun MonthSelector(
    currentYear: Int,
    currentMonth: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val caretImageSize = 16.dp

    // 시스템의 현재 연도와 월
    val calendar = Calendar.getInstance()
    val systemYear = calendar.get(Calendar.YEAR)
    val systemMonth = calendar.get(Calendar.MONTH) + 1

    // 월 표시 텍스트 구성
    val monthDisplayText = if (currentYear == systemYear) {
        stringResource(id = R.string.calendar_month, currentMonth)
    } else {
        stringResource(id = R.string.calendar_year_month, currentYear, currentMonth)
    }

    // 오른쪽 화살표 색상을 조건에 따라 변경:
    // 올해의 이번 달이면 비활성화된 색상(예: grey2), 아니면 활성화 색상(primary)
    val rightArrowColor = if (currentYear == systemYear && currentMonth == systemMonth) {
        LocalMulGaColors.current.grey3
    } else {
        LocalMulGaColors.current.grey1
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_util_caret_left),
            contentDescription = null,
            modifier = Modifier
                .size(caretImageSize)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onPrevClick() },
            colorFilter = ColorFilter.tint(LocalMulGaColors.current.grey1)
        )
        Text(
            text = monthDisplayText,
            textAlign = TextAlign.Start,
            style = LocalMulGaTypography.current.subtitle,
            color = LocalMulGaColors.current.grey1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_util_caret_right),
            contentDescription = null,
            modifier = Modifier
                .size(caretImageSize)
                .clickable(
                    // 만약 비활성화 상태라면 클릭 액션을 막을 수도 있음
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // 비활성화 상태라면 아무 작업도 수행하지 않도록 할 수 있음
                    if (!(currentYear == systemYear && currentMonth == systemMonth)) {
                        onNextClick()
                    }
                },
            colorFilter = ColorFilter.tint(rightArrowColor)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MonthSelectorPreview() {
    MonthSelector(
        currentYear = 2025,  // 예시로 올해가 아닌 연도로 설정
        currentMonth = 3,
        onPrevClick = { /* 테스트용 액션 */ },
        onNextClick = { /* 테스트용 액션 */ }
    )
}