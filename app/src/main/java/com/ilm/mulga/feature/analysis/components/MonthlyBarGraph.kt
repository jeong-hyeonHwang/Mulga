package com.ilm.mulga.feature.analysis.components

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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme
import java.text.NumberFormat
import kotlin.math.ceil

@Composable
fun MonthlyBarGraph(
    modifier: Modifier = Modifier,
    amount: List<Float>, // Custom heights for each bar
    currentMonth: Int // Single int for the current month
) {
    // Compute the average of the barHeights
    val averageHeight = NumberFormat.getInstance().format(ceil(amount.average() / 10000).toFloat())

    // Fetch colors from MaterialTheme here, outside of Canvas
    val grey2Color = MulGaTheme.colors.grey2
    val grey3Color = MulGaTheme.colors.grey3
    val grey4Color = MulGaTheme.colors.grey4
    val primaryColor = MulGaTheme.colors.primary
    val grey1Color = MulGaTheme.colors.grey1 // Added grey1 color for text

    // Fetch typography details from the theme
    val labelTextStyle = MulGaTheme.typography.title // You can change this to a different style if needed
    val labelTextSize = labelTextStyle.fontSize.value

    // Generate the labels for the current month and the past 5 months
    val labelTexts = generateMonthLabels(currentMonth)

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
            // Title text above the graph, showing the average
            val annotatedText = buildAnnotatedString {
                append("월 평균\n")
                withStyle(style = SpanStyle(color = primaryColor)) { // Use SpanStyle for color styling
                    append("${averageHeight.toInt()}만원") // Apply color to average amount
                }
                append(" 정도 써요")
            }

            Text(
                text = annotatedText, // Using the annotated string with different styles
                style = MulGaTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 64.dp) // Optional: Add spacing below the text
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth() // Make the canvas take up the full width
                    .height(250.dp) // We will dynamically calculate the height based on the width
            ) {
                // Access the size of the canvas inside the draw block
                val width = size.width
                val height = size.height

                // Set the max height to be half of the canvas width
                val maxHeight = width / 2

                // Bottom padding for the bar only (32.dp) and convert to pixels
                val bottomPadding = 32.dp.toPx()
                val availableHeightForBars = height - bottomPadding

                val xAxisWidth = width // 90% of the canvas width
                val xAxisStart = (width - xAxisWidth) / 2 // Center the x-axis

                // Number of bars
                val numberOfBars = labelTexts.size // Dynamic based on the number of labels
                val paddingBetweenBars = 22.dp.toPx() // Padding between each bar in pixels

                // Calculating the width of each bar
                val availableWidthForBars = xAxisWidth - (paddingBetweenBars * numberOfBars)
                val barWidth = availableWidthForBars / numberOfBars

                // Corner radius for the top corners only
                val cornerRadius = 6.dp.toPx()

                // Drawing the bars with rounded top corners using Path
                for (i in 0 until numberOfBars) {
                    val barHeight = amount.getOrElse(i) { availableHeightForBars } // Use custom height, default if missing
                    val scaledHeight = (barHeight / amount.maxOrNull()!!) * maxHeight // Scale the height proportionally

                    val left = xAxisStart + i * (barWidth + paddingBetweenBars) + (paddingBetweenBars / 2) // Start position of the bar
                    val top = availableHeightForBars - scaledHeight // Adjust the height of each bar

                    // Select color based on the bar index
                    val barColor = if (i < 5) {
                        grey3Color
                    } else {
                        primaryColor
                    }

                    // Create a path to draw the rounded rectangle with custom top corners
                    val path = Path().apply {
                        moveTo(left, availableHeightForBars) // Move to the start position of the bar (bottom-left corner)
                        lineTo(left, top + cornerRadius) // Draw the bottom-left edge (straight)
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
                        lineTo(left + barWidth - cornerRadius, top) // Draw the top edge (straight line to top-right corner)
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
                        lineTo(left + barWidth, availableHeightForBars) // Draw the right edge (straight down to bottom-right corner)
                        lineTo(left, availableHeightForBars) // Draw the bottom-right corner (straight)
                        close() // Close the path
                    }

                    // Fill the path with the selected color
                    drawPath(path, barColor)

                    // Draw the bar height value above the bar
                    val heightText = NumberFormat.getInstance().format(ceil(amount[i] / 10000).toInt()) // Convert to int and display as a string
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            heightText, // The height value text
                            left + barWidth / 2, // Center the text horizontally above the bar
                            top - 24f, // Position the text just above the top of the bar
                            android.graphics.Paint().apply {
                                textSize = labelTextSize // Set the text size from the theme
                                color = grey1Color.toArgb() // Set the color of the label text
                                textAlign = android.graphics.Paint.Align.CENTER // Center align the text
                            }
                        )
                    }

                    // Draw the label below the bar
                    val label = labelTexts.getOrElse(i) { "Label ${i + 1}" } // Get the label from the list
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label, // The label text
                            left + barWidth / 2, // Center the label horizontally under the bar
                            availableHeightForBars + 48f, // Position the label just below the bar
                            android.graphics.Paint().apply {
                                textSize = labelTextSize // Set the text size from the theme
                                color = grey1Color.toArgb() // Set the color of the label text
                                textAlign = android.graphics.Paint.Align.CENTER // Center align the text
                            }
                        )
                    }
                }

                // Drawing the x-axis at the bottom (with padding)
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
