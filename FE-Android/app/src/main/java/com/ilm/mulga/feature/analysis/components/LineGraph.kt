package com.ilm.mulga.feature.analysis.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.abs
import kotlin.math.ceil

@Composable
fun MonthlyLineGraph(
    modifier: Modifier = Modifier,
    year: Int, // Year for the graph
    month: Int, // Month for the graph
    prevMonthData: List<Float>, // Data for the first line (last month)
    currMonthData: List<Float>, // Data for the second line (current month)
) {
    // Fetch colors from MaterialTheme here, outside of Canvas
    val grey1Color = MulGaTheme.colors.grey1
    val grey2Color = MulGaTheme.colors.grey2
    val grey4Color = MulGaTheme.colors.grey4
    val primaryColor = MulGaTheme.colors.primary

    val captionSize = MulGaTheme.typography.caption.fontSize

    // Get screen width to calculate proportional height
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    // Calculate desired graph height based on screen width
    val desiredGraphHeight = screenWidth * 2 / 5

    Log.d("curr", currMonthData.toString())

    // Define the height of the rounded border container to wrap both text and graph
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
            // Get current date for comparison
            val today = LocalDate.now()

            // Get the value to display for current month
            val valueToDisplay = if (today.year == year && today.monthValue == month) {
                // If today is within the month, use today's value or the last available value
                val todayIndex = today.dayOfMonth - 1 // Index starts from 0
                currMonthData.getOrElse(todayIndex) { currMonthData.last() }
            } else {
                // Otherwise, use the last value of line2Data
                currMonthData.last()
            }

            // Get the corresponding value from prevMonthData for the same day
            val line1Value = if (today.year == year && today.monthValue == month) {
                prevMonthData.getOrElse(today.dayOfMonth - 1) { prevMonthData.last() }
            } else {
                prevMonthData.last()
            }

            // Calculate the difference
            val difference = valueToDisplay - line1Value

            // Title text above the graph, showing the averages
            val annotatedText = buildAnnotatedString {
                if (today.year == year && today.monthValue == month) {
                    append("오늘까지 ${NumberFormat.getInstance().format(ceil(valueToDisplay / 10000))}만원 썼어요\n")
                } else {
                    append("${NumberFormat.getInstance().format(ceil(valueToDisplay / 10000))}만원 씀\n")
                }

                withStyle(style = SpanStyle(color = grey1Color, fontSize = captionSize)) {
                    append("지난달보다 ")
                }
                withStyle(style = SpanStyle(color = primaryColor, fontSize = captionSize)) {
                    append("${NumberFormat.getInstance().format(ceil(abs(difference) / 10000))}만원 ${if (difference >= 0) "더" else "덜"}")
                }
                withStyle(style = SpanStyle(color = grey1Color, fontSize = captionSize)) {
                    append(" ${if (today.year == year && today.monthValue == month) "쓰는 중" else "씀"}")
                }
            }

            Text(
                text = annotatedText,
                style = MulGaTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(desiredGraphHeight + 40.dp) // Add extra height for legends
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Canvas drawing logic
                    val width = size.width
                    val height = size.height

                    // Reserve space for legends at the bottom
                    val legendHeight = 40.dp.toPx()

                    // Bottom padding for the graph
                    val bottomPadding = 32.dp.toPx()
                    // Adjust available height to include space for the legend
                    val availableHeightForGraph = height - bottomPadding - legendHeight

                    val xAxisWidth = width
                    val xAxisStart = 0f // Start from the left edge

                    // Number of points (one for each day)
                    val numberOfPoints = 31

                    // Calculate spacing between points
                    val paddingBetweenPoints = (width / (numberOfPoints * 2)).coerceAtMost(22.dp.toPx())

                    // Calculate point width based on available space
                    val availableWidthForPoints = width - (paddingBetweenPoints * (numberOfPoints - 1))
                    val pointWidth = availableWidthForPoints / (numberOfPoints - 1)

                    // Helper function to get scaled data points
                    fun scaleDataPoint(value: Float): Float {
                        val maxDataValue = maxOf(
                            prevMonthData.maxOrNull() ?: 0f,
                            currMonthData.maxOrNull() ?: 0f
                        ).takeIf { it > 0 } ?: 1f
                        return (value / maxDataValue) * availableHeightForGraph
                    }

                    // Check if today is within the given year and month
                    val isTodayInMonth = today.year == year && today.monthValue == month

                    // Draw the lines for both data sets (previous month first, then current month)
                    for (lineIndex in 0..1) {
                        val data = if (lineIndex == 0) prevMonthData else currMonthData
                        val lineColor = if (lineIndex == 0) grey2Color else primaryColor

                        // Limit the number of points for current month data (up to today) if today is within the given month
                        val dataLimit = if (lineIndex == 1 && isTodayInMonth) {
                            today.dayOfMonth.coerceAtMost(data.size)
                        } else {
                            data.size
                        }

                        var previousPoint: Offset? = null

                        for (i in 0 until dataLimit) {
                            val x = i * (pointWidth + paddingBetweenPoints)
                            val y = availableHeightForGraph - scaleDataPoint(data[i])

                            // Draw the line only if there's a previous point
                            previousPoint?.let {
                                drawLine(
                                    color = lineColor,
                                    start = it,
                                    end = Offset(x, y),
                                    strokeWidth = 3f
                                )
                            }

                            // Update the previous point to the current one
                            previousPoint = Offset(x, y)

                            // If it's the last point of current month line and we're within the current month, add a circle
                            if (lineIndex == 1 && i == dataLimit - 1 && isTodayInMonth) {
                                drawCircle(
                                    color = lineColor,
                                    radius = 8f,
                                    center = Offset(x, y)
                                )
                            }
                        }
                    }

                    // Draw the labels for the x-axis (first, last day of the month, and today if applicable)
                    val firstDayOfMonth = LocalDate.of(year, month, 1)
                    val lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth())

                    // List of days to draw: first day of the month, last day of the month, and today (if within the same month)
                    val daysToLabel = mutableListOf(firstDayOfMonth, lastDayOfMonth)
                    if (isTodayInMonth) {
                        daysToLabel.add(today) // Add today if it belongs to the specified month and year
                    }

                    val padding = 12.dp.toPx()

                    daysToLabel.forEach { day ->
                        // Calculate x position for the day label
                        val x = (day.dayOfMonth - 1) * (pointWidth + paddingBetweenPoints)

                        // Ensure the label stays within the graph width
                        val adjustedX = x.coerceIn(padding, width - padding)

                        drawContext.canvas.nativeCanvas.apply {
                            drawText(
                                day.format(DateTimeFormatter.ofPattern("M.d")),
                                adjustedX,
                                availableHeightForGraph + 64f,
                                android.graphics.Paint().apply {
                                    textSize = 36f
                                    color = grey2Color.toArgb()
                                    textAlign = android.graphics.Paint.Align.CENTER
                                }
                            )
                        }
                    }

                    // Draw the legend at the bottom (now within the canvas bounds)
                    val legendStartX = (width / 2) - 65.dp.toPx()
                    val legendY = height - 16.dp.toPx() // Position from the bottom of the canvas

                    // Draw the first legend (current month line)
                    drawLine(
                        color = primaryColor,
                        start = Offset(legendStartX, legendY),
                        end = Offset(legendStartX + 30.dp.toPx(), legendY),
                        strokeWidth = 14f
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${month}월",
                            legendStartX + 40.dp.toPx(),
                            legendY + 4.dp.toPx(),
                            android.graphics.Paint().apply {
                                textSize = 36f
                                color = grey1Color.toArgb()
                                textAlign = android.graphics.Paint.Align.LEFT
                            }
                        )
                    }

                    // Draw the second legend (last month line)
                    drawLine(
                        color = grey2Color,
                        start = Offset(legendStartX + 70.dp.toPx(), legendY),
                        end = Offset(legendStartX + 100.dp.toPx(), legendY),
                        strokeWidth = 14f
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${if (month == 1) 12 else month - 1}월",
                            legendStartX + 110.dp.toPx(),
                            legendY + 4.dp.toPx(),
                            android.graphics.Paint().apply {
                                textSize = 36f
                                color = grey1Color.toArgb()
                                textAlign = android.graphics.Paint.Align.LEFT
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonthlyLineGraphPreview() {
    val prevMonthData = List(31) { (1000..5000).random().toFloat() }  // Random data for previous month (31 days)
    val currMonthData = List(31) { (2000..6000).random().toFloat() }  // Random data for current month (31 days)
    val currentYear = 2025
    val currentMonth = 4  // For example, April

    MulGaTheme {
        // Remove fillMaxSize() and use a more appropriate modifier
        MonthlyLineGraph(
            modifier = Modifier.wrapContentSize(),
            year = currentYear,
            month = currentMonth,
            prevMonthData = prevMonthData,
            currMonthData = currMonthData
        )
    }
}