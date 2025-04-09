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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
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

    Log.d("curr", currMonthData.toString())

    // Define the height of the rounded border container to wrap both text and graph
    Box(
        modifier = modifier
            .padding(16.dp) // Optional: Add padding around the entire Box
            .border(
                width = 1.dp, // Border thickness
                color = grey4Color, // Border color
                shape = RoundedCornerShape(12.dp) // Correctly use RoundedCornerShape for rounded corners
            )
            .clip(RoundedCornerShape(12.dp)) // Ensure the content also respects the rounded corners
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Make sure the Column takes up all available space
                .padding(16.dp) // Padding inside the Box around the content
        ) {
            // Get the value to display for "aaa" in the annotated text
            val today = LocalDate.now()
            val valueToDisplay = if (today.year == year && today.monthValue == month) {
                // If today is within the month, use today's value or the last available value
                val todayIndex = today.dayOfMonth - 1 // Index starts from 0
                currMonthData.getOrElse(todayIndex) { currMonthData.last() }
            } else {
                // Otherwise, use the last value of line2Data
                currMonthData.last()
            }

            // Get the corresponding value from line1Data for the same day
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

                withStyle(style = SpanStyle(color = grey1Color, fontSize = captionSize)) { // Use SpanStyle for color styling
                    append("지난달보다 ") // Apply color to the difference
                }
                withStyle(style = SpanStyle(color = primaryColor, fontSize = captionSize)) { // Use SpanStyle for color styling
                    append("${NumberFormat.getInstance().format(ceil(abs(difference) / 10000))}만원 ${if (difference >= 0) "더" else "덜"}") // Apply color to the difference
                }
                withStyle(style = SpanStyle(color = grey1Color, fontSize = captionSize)) { // Use SpanStyle for color styling
                    append(" ${if (today.year == year && today.monthValue == month) "쓰는 중" else "씀"}") // Apply color to the difference
                }
            }

            Text(
                text = annotatedText, // Using the annotated string with different styles
                style = MulGaTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp) // Optional: Add spacing below the text
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth() // Make the canvas take up the full width
                    .height(150.dp) // Height of the line graph
            ) {
                // Access the size of the canvas inside the draw block
                val width = size.width
                val height = size.height

                // Bottom padding for the graph (32.dp)
                val bottomPadding = 32.dp.toPx()
                val availableHeightForGraph = height - bottomPadding

                val xAxisWidth = width // 90% of the canvas width
                val xAxisStart = (width - xAxisWidth) / 2 // Center the x-axis

                // Number of points (bars)
                val numberOfPoints = 31 // Set this to 31 for each day of the month

                // Padding between the points
                val paddingBetweenPoints = 22.dp.toPx() // Padding between each data point in pixels

                // Calculating the width of each point
                val availableWidthForPoints = xAxisWidth - (paddingBetweenPoints * (numberOfPoints - 1))
                val pointWidth = availableWidthForPoints / (numberOfPoints - 1)

                // Helper function to get scaled data points
                fun scaleDataPoint(value: Float): Float {
                    val maxDataValue = maxOf(prevMonthData.maxOrNull() ?: 0f, currMonthData.maxOrNull() ?: 0f)
                    return (value / maxDataValue) * availableHeightForGraph
                }

                // Check if today is within the given year and month
                val isTodayInMonth = today.year == year && today.monthValue == month

                // Draw the lines for both data sets (line2 and line1) in this order to layer correctly
                for (lineIndex in 0..1) {
                    // Swap logic: line 0 should be for prevMonthData, and line 1 should be for currMonthData
                    val data = if (lineIndex == 0) prevMonthData else currMonthData // Corrected: prevMonthData is line 0, currMonthData is line 1
                    val lineColor = if (lineIndex == 0) grey2Color else primaryColor // Color for the respective line (prevMonthData and currMonthData)

                    // Limit the number of points for line1 (up to today) if today is within the given month and year
                    val dataLimit = if (lineIndex == 1 && isTodayInMonth) today.dayOfMonth else data.size

                    // Start drawing from the first data point
                    var previousPoint: Offset? = null

                    for (i in 0 until dataLimit) {
                        val x = xAxisStart + i * pointWidth + paddingBetweenPoints * i
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

                        // If it's the last point and we're drawing Line1, add a circle at the end
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
                    // Adjust the x coordinate by adding the left and right padding
                    val x = xAxisStart + (day.dayOfMonth - 1) * pointWidth + paddingBetweenPoints * (day.dayOfMonth - 1) + padding

                    // Ensure the label stays within the graph width by limiting it with right padding
                    val adjustedX = x.coerceIn(padding, width - padding)

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            day.format(DateTimeFormatter.ofPattern("M.d")), // Format date as M.d (no leading zero)
                            adjustedX,
                            availableHeightForGraph + 72f, // Position the label just below the graph
                            android.graphics.Paint().apply {
                                textSize = 36f
                                color = grey2Color.toArgb()
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                    }
                }

                // Draw the legend at the bottom
                val legendStartX = (width / 2) - 65.dp.toPx()
                val legendY = height + 16.dp.toPx()

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
