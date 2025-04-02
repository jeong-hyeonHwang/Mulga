package com.ilm.mulga.feature.calendar.components

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
import com.ilm.mulga.R
import com.ilm.mulga.ui.theme.MulGaTheme
import java.util.Calendar

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MonthSelector(
    currentYear: Int,
    currentMonth: Int,
    onPrevClick: suspend () -> Unit,
    onNextClick: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val caretImageSize = 16.dp

    // 시스템의 현재 연도와 월
    val calendar = Calendar.getInstance()
    val systemYear = calendar.get(Calendar.YEAR)
    val systemMonth = calendar.get(Calendar.MONTH) + 1

    val monthDisplayText = if (currentYear == systemYear) {
        stringResource(id = R.string.calendar_month, currentMonth)
    } else {
        stringResource(id = R.string.calendar_year_month, currentYear, currentMonth)
    }

    val rightArrowColor = if (currentYear == systemYear && currentMonth == systemMonth) {
        MulGaTheme.colors.grey3
    } else {
        MulGaTheme.colors.grey1
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
                ) {
                    coroutineScope.launch { onPrevClick() }
                },
            colorFilter = ColorFilter.tint(MulGaTheme.colors.grey1)
        )
        Text(
            text = monthDisplayText,
            textAlign = TextAlign.Start,
            style = MulGaTheme.typography.subtitle,
            color = MulGaTheme.colors.grey1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_util_caret_right),
            contentDescription = null,
            modifier = Modifier
                .size(caretImageSize)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (!(currentYear == systemYear && currentMonth == systemMonth)) {
                        coroutineScope.launch { onNextClick() }
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