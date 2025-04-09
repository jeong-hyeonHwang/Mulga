package com.ilm.mulga.feature.analysis.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import com.ilm.mulga.ui.theme.MulGaTheme
import java.text.NumberFormat

@Composable
fun DonutChart(slices: List<Int>, total: Int, modifier: Modifier = Modifier) {
    // Define a set of 6 colors for the donut chart
    val colors = listOf(
        Color(0xFF006EFF),
        Color(0xFF00B0FF),
        Color(0xFF8BC34A),
        Color(0xFFFAE000),
        Color(0xFFFF9800),
        Color(0xFF888787) // gray color
    )

    // If total is zero, create a single slice with the last gray color
    val adjustedSlices = if (total == 0) {
        listOf(1) // A slice with a value of 1 to be drawn with the gray color
    } else {
        slices
    }

    Box(modifier = modifier) {
        // Animate the angle for each slice
        val gapAngle = 3f
        val totalValue = adjustedSlices.sum().toDouble()
        val startAngle = -90f // Start from top

        // Set up animated sweep angles for each slice
        val animatedSlices = adjustedSlices.map { slice ->
            animateFloatAsState(
                targetValue = ((slice.toDouble() / totalValue) * (360f - adjustedSlices.size * gapAngle)).toFloat()
            )
        }

        // Canvas for drawing the donut chart
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Gap angle for spacing between slices
            var currentSweepAngle = startAngle

            // Loop through each slice and animate its appearance
            animatedSlices.forEachIndexed { index, animatedValue ->
                val sweepAngle = animatedValue.value
                val sliceColor = if (total == 0) colors.last() else (if (index < colors.size) colors[index] else colors.last())

                drawDonutSliceWithGap(
                    startAngle = currentSweepAngle,
                    sweepAngle = sweepAngle,
                    color = sliceColor,
                    radius = size.minDimension / 3,
                    thickness = size.minDimension / 9
                )

                // Update the current angle for the next slice, adding the gap angle after each slice
                currentSweepAngle += sweepAngle + gapAngle
            }
        }

        // Display the text in the center
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(), // Fill the available space
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Vertically center the content
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

    // Outer radius (the outer edge of the donut)
    val outerRadius = radius
    // Inner radius (the hole in the middle of the donut)
    val innerRadius = radius - thickness

    // Draw the outer arc (this will be the outside of the slice)
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center.copy(x = center.x - outerRadius, y = center.y - outerRadius),
        size = size.copy(width = outerRadius * 2, height = outerRadius * 2),
        style = Stroke(width = thickness)
    )

    // Draw the inner arc (this will create the hole)
    drawArc(
        color = Color.Transparent, // Transparent color to create the hole
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = center.copy(x = center.x - innerRadius, y = center.y - innerRadius),
        size = size.copy(width = innerRadius * 2, height = innerRadius * 2),
        style = Stroke(width = thickness)
    )
}
