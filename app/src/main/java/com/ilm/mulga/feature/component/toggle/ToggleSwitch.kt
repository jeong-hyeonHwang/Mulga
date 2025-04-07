package com.ilm.mulga.feature.component.toggle

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

@SuppressLint("RememberReturnType")
@Composable
fun ToggleSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    // 사용자가 커스텀할 수 있도록 텍스트를 파라미터로
    firstLabel: String = "달력",
    secondLabel: String = "목록"
) {
    // 상단에 선언하는 값들
    val trackWidth = 84.dp
    val trackHeight = 32.dp
    val thumbWidth = 42.dp
    val thumbHeight = 24.dp
    val horizontalPadding = 4.dp
    val thumbVerticalPadding = (trackHeight - thumbHeight) / 2

    // 선택된 옵션에 따라 Thumb의 xOffset을 애니메이션
    val xOffset by animateDpAsState(
        targetValue = if (selectedIndex == 0) {
            horizontalPadding // 왼쪽 여백
        } else {
            trackWidth - thumbWidth - horizontalPadding // 오른쪽 여백
        }
    )

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(trackHeight)
            .clip(RoundedCornerShape(15.dp))
            .background(MulGaTheme.colors.grey4)
    ) {
        // 흰색 Thumb (슬라이딩 영역)
        Box(
            modifier = Modifier
                .offset(x = xOffset, y = thumbVerticalPadding)
                .width(thumbWidth)
                .height(thumbHeight)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
        )

        // 옵션들
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null, // ripple 및 hover 효과 제거
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    // Row 자체를 클릭해도 아무 일도 없도록
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = firstLabel,
                style = MulGaTheme.typography.caption,
                color = if (selectedIndex == 0) MulGaTheme.colors.primary else Color.Gray,
                modifier = Modifier
                    .clickable(
                        indication = null, // ripple 및 hover 효과 제거
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onOptionSelected(0)
                    },
                textAlign = TextAlign.Center
            )

            Text(
                text = secondLabel,
                style = MulGaTheme.typography.caption,
                color = if (selectedIndex == 1) MulGaTheme.colors.primary else MulGaTheme.colors.grey2,
                modifier = Modifier
                    .clickable(
                        indication = null, // ripple 및 hover 효과 제거
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onOptionSelected(1)
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToggleSwitchPreview() {
    var selectedIndex by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "현재 선택: ${if (selectedIndex == 0) "첫 번째" else "두 번째"}")

        ToggleSwitch(
            selectedIndex = selectedIndex,
            onOptionSelected = { newIndex ->
                selectedIndex = newIndex
            },
            firstLabel = "달력",
            secondLabel = "목록"
        )


        // TODO: 글자수가 다른 경우 Margin값 수정
        ToggleSwitch(
            selectedIndex = selectedIndex,
            onOptionSelected = { newIndex ->
                selectedIndex = newIndex
            },
            firstLabel = " ON",
            secondLabel = " OFF"
        )

        ToggleSwitch(
            selectedIndex = selectedIndex,
            onOptionSelected = { newIndex ->
                selectedIndex = newIndex
            },
            firstLabel = "수입",
            secondLabel = "지출"
        )
    }
}
