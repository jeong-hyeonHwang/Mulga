package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme
import java.text.NumberFormat
import kotlin.math.ceil
import kotlin.math.max

@Composable
fun MonthlyBarGraph(
    modifier: Modifier = Modifier,
    amount: List<Float>,
    currentMonth: Int
) {
    val averageHeight = NumberFormat.getInstance().format(ceil(amount.average() / 10000).toFloat())

    // Theme colors
    val grey2Color = MulGaTheme.colors.grey2
    val grey3Color = MulGaTheme.colors.grey3
    val grey4Color = MulGaTheme.colors.grey4
    val primaryColor = MulGaTheme.colors.primary
    val grey1Color = MulGaTheme.colors.grey1

    val labelTextStyle = MulGaTheme.typography.title
    val labelTextSize = labelTextStyle.fontSize.value
    val labelTexts = generateMonthLabels(currentMonth)

    // Get the screen width to calculate the canvas height
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    // Calculate desired height based on screen width
    val desiredGraphHeight = screenWidthDp / 2

    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = grey4Color,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title text
            val annotatedText = buildAnnotatedString {
                append("월 평균\n")
                withStyle(style = SpanStyle(color = primaryColor)) {
                    append("${averageHeight.toInt()}만원")
                }
                append(" 정도 써요")
            }

            Text(
                text = annotatedText,
                style = MulGaTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp) // Add some spacing below the text
            )

            // Use a proportional height based on screen width
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(desiredGraphHeight) // This makes the canvas height proportional to screen width
            ) {
                val width = size.width
                val height = size.height

                // The max bar height is now based on the canvas height (which is proportional to width)
                val maxBarHeight = height * 0.75f // Leave room for labels

                val bottomPadding = 32.dp.toPx()
                val availableHeightForBars = height - bottomPadding

                val xAxisWidth = width
                val xAxisStart = 0f // Start from the left edge

                // Number of bars
                val numberOfBars = labelTexts.size
                val paddingBetweenBars = 22.dp.toPx()

                // Calculating the width of each bar
                val availableWidthForBars = xAxisWidth - (paddingBetweenBars * numberOfBars)
                val barWidth = availableWidthForBars / numberOfBars

                // Corner radius for the top corners only
                val cornerRadius = 6.dp.toPx()

                // Minimum height threshold for showing curved corners
                // This is twice the corner radius to ensure there's enough space for the curves
                val minHeightForCurves = cornerRadius * 2

                // Drawing the bars
                for (i in 0 until numberOfBars) {
                    val barValue = amount.getOrElse(i) { 0f }
                    val maxValue = amount.maxOrNull() ?: 1f
                    val scaledHeight = (barValue / maxValue) * maxBarHeight

                    val left = xAxisStart + i * (barWidth + paddingBetweenBars) + paddingBetweenBars / 2
                    val top = availableHeightForBars - scaledHeight

                    // Select color based on the bar index
                    val barColor = if (i < 5) {
                        grey3Color
                    } else {
                        primaryColor
                    }

                    // Check if bar height is sufficient for curved corners
                    if (scaledHeight > minHeightForCurves) {
                        // Create path for rounded rectangle with curved top corners
                        val path = Path().apply {
                            moveTo(left, availableHeightForBars)
                            lineTo(left, top + cornerRadius)
                            arcTo(
                                rect = androidx.compose.ui.geometry.Rect(
                                    left = left,
                                    top = top,
                                    right = left + cornerRadius * 2,
                                    bottom = top + cornerRadius * 2
                                ),
                                startAngleDegrees = 180f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                            lineTo(left + barWidth - cornerRadius, top)
                            arcTo(
                                rect = androidx.compose.ui.geometry.Rect(
                                    left = left + barWidth - cornerRadius * 2,
                                    top = top,
                                    right = left + barWidth,
                                    bottom = top + cornerRadius * 2
                                ),
                                startAngleDegrees = 270f,
                                sweepAngleDegrees = 90f,
                                forceMoveTo = false
                            )
                            lineTo(left + barWidth, availableHeightForBars)
                            lineTo(left, availableHeightForBars)
                            close()
                        }

                        drawPath(path, barColor)
                    } else if (scaledHeight > 0) {
                        // For small bars, draw a simple rectangle without curved corners
                        drawRect(
                            color = barColor,
                            topLeft = Offset(left, top),
                            size = androidx.compose.ui.geometry.Size(barWidth, scaledHeight)
                        )
                    } else {
                        // For zero values, draw a minimal indicator line
                        drawLine(
                            color = barColor,
                            start = Offset(left, availableHeightForBars),
                            end = Offset(left + barWidth, availableHeightForBars),
                            strokeWidth = 2f
                        )
                    }

                    // Draw values above bars
                    val heightText = NumberFormat.getInstance().format(ceil(barValue / 10000).toInt())

                    // Position text appropriately based on bar height
                    val textYPosition = if (scaledHeight > 24f) {
                        top - 24f // Normal position above the bar
                    } else {
                        // For very small or zero bars, position text above the axis
                        availableHeightForBars - max(scaledHeight, 0f) - 24f
                    }

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            heightText,
                            left + barWidth / 2,
                            textYPosition,
                            android.graphics.Paint().apply {
                                textSize = labelTextSize
                                color = grey1Color.toArgb()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }

                    // Draw labels below bars
                    val label = labelTexts[i]
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label,
                            left + barWidth / 2,
                            availableHeightForBars + 48f,
                            android.graphics.Paint().apply {
                                textSize = labelTextSize
                                color = grey1Color.toArgb()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }

                // Draw x-axis
                drawLine(
                    color = grey2Color,
                    start = Offset(x = xAxisStart, y = availableHeightForBars),
                    end = Offset(x = xAxisStart + xAxisWidth, y = availableHeightForBars),
                    strokeWidth = 2f
                )
            }
        }
    }
}

// Function to generate the labels for the current month and the past 5 months
fun generateMonthLabels(currentMonth: Int): List<String> {
    val labels = mutableListOf<String>()
    for (i in 0 until 6) {
        val month = (currentMonth - i + 12) % 12
        val monthName = when (month) {
            0 -> "12월"
            1 -> "1월"
            2 -> "2월"
            3 -> "3월"
            4 -> "4월"
            5 -> "5월"
            6 -> "6월"
            7 -> "7월"
            8 -> "8월"
            9 -> "9월"
            10 -> "10월"
            11 -> "11월"
            else -> "Unknown"
        }
        labels.add(monthName)
    }
    return labels.reversed() // Reverse to show the current month as the last bar
}

@Preview(showBackground = true)
@Composable
fun MonthlyBarGraphPreview() {
    val amounts = listOf(120000f, 5000f, 0f, 90000f, 180000f, 110000f) // Sample with zero and small value
    MonthlyBarGraph(
        modifier = Modifier.fillMaxWidth(),
        amount = amounts,
        currentMonth = 4 // Current month as May (5th month in the year)
    )
}