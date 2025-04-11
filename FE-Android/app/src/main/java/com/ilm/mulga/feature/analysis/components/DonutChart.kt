package com.ilm.mulga.feature.analysis.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.ilm.mulga.presentation.model.DonutSliceInfo
import com.ilm.mulga.presentation.model.type.Category
import com.ilm.mulga.ui.theme.MulGaTheme
import java.text.NumberFormat

@Composable
fun DonutChart(slices: List<DonutSliceInfo>, total: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val defaultColor = MulGaTheme.colors.grey2
    val adjustedSlices = if (total == 0) {
        listOf(DonutSliceInfo(Category.ETC, value = 1))
    } else {
        slices
    }

    Box(modifier = modifier) {
        // 슬라이스 사이의 gap 각도
        val gapAngle = 3f
        val totalValue = adjustedSlices.sumOf { it.value }.toDouble()
        val startAngle = -90f + gapAngle / 2 // 상단부터 시작

        // 각 슬라이스에 대해 애니메이션 처리 (value를 바탕으로 각도 계산)
        val animatedSlices = adjustedSlices.map { slice ->
            animateFloatAsState(
                targetValue = ((slice.value.toDouble() / totalValue) * 360).toFloat()
            )
        }

        // Canvas를 이용해 도넛 차트 그리기
        Canvas(
            modifier = Modifier
                .size(200.dp) // 원하는 크기
                .aspectRatio(1f) // 정사각형 (원형) 유지
                .align(Alignment.Center)
        ) {
            var currentSweepAngle = startAngle

            animatedSlices.forEachIndexed { index, animatedValue ->
                val sweepAngle = animatedValue.value
                // total이 0이면 회색, 그렇지 않으면 각 SliceInfo에 지정된 색상 사용
                val sliceColor = if (total == 0) {
                    defaultColor
                } else {
                    Color(ContextCompat.getColor(context, slices[index].category.colorResId))
                }
                drawDonutSliceWithGap(
                    startAngle = currentSweepAngle,
                    sweepAngle = sweepAngle - gapAngle,
                    color = sliceColor,
                    radius = size.minDimension * 0.6f,
                    thickness = size.minDimension * 0.22f
                )

                currentSweepAngle += sweepAngle
            }
        }

        // 중앙 텍스트 표시
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .size(120.dp), // 내부 원과 맞춤
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "지출 금액",
                style = MulGaTheme.typography.bodyMedium,
                color = MulGaTheme.colors.grey1,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${NumberFormat.getNumberInstance().format(total)}원",
                style = MulGaTheme.typography.title,
                color = MulGaTheme.colors.grey1,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun DrawScope.drawDonutSliceWithGap(
    startAngle: Float,
    sweepAngle: Float,
    color: Color,
    radius: Float,
    thickness: Float
) {
    val center = center

    // 외곽 원 (도넛의 외부)
    val outerRadius = radius
    // 내부 원 (도넛의 구멍)
    val innerRadius = radius - thickness

    // 외부 호 그리기
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center.copy(x = center.x - outerRadius, y = center.y - outerRadius),
        size = size.copy(width = outerRadius * 2, height = outerRadius * 2),
        style = Stroke(width = thickness)
    )

    // 내부 호(구멍) 부분 그리기: 투명 색상으로 처리
    drawArc(
        color = Color.Transparent,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center.copy(x = center.x - innerRadius, y = center.y - innerRadius),
        size = size.copy(width = innerRadius * 2, height = innerRadius * 2),
        style = Stroke(width = thickness)
    )
}
