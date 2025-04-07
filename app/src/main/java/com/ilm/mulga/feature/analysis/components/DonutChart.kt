package com.ilm.mulga.feature.analysis.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilm.mulga.ui.theme.MulGaTheme

data class DonutSlice(val value: Float)

@Composable
fun DonutChart(slices: List<DonutSlice>, modifier: Modifier = Modifier) {
    // Define a set of 6 colors for the donut chart
    val colors = listOf(
        Color(0xFF006EFF),
        Color(0xFF00B0FF),
        Color(0xFF8BC34A),
        Color(0xFFFAE000),
        Color(0xFFFF9800),
        Color(0xFF888787)
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Convert the sum to Double
            val totalValue = slices.sumOf { it.value.toDouble() }
            val startAngle = -90f // Start from top
            var currentAngle = startAngle
            val radius = size.minDimension / 3
            val thickness = size.minDimension / 9 // Thickness of the donut's ring
            val gapAngle = 3f // Angle for the gap between slices

            slices.forEachIndexed { index, slice ->
                // Get the color for the slice (reuse the 6th color if there are more than 6 slices)
                val sliceColor = if (index < colors.size) colors[index] else colors.last()

                // Calculate the sweep angle for each slice
                val sweepAngle = (slice.value.toDouble() / totalValue) * 360f - gapAngle // Subtract gap to create space between slices

                // Draw each slice with the gap
                drawDonutSliceWithGap(
                    startAngle = currentAngle,
                    sweepAngle = sweepAngle.toFloat(),
                    color = sliceColor,
                    radius = radius,
                    thickness = thickness
                )

                // Move the current angle by the sweep angle + the gap angle
                currentAngle += sweepAngle.toFloat() + gapAngle
            }
        }

        // Calculate the total value to display as text
        val totalAmount = slices.sumOf { it.value.toDouble() }.toInt()

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
                text = "${totalAmount}원",
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

@Preview(showBackground = true)
@Composable
fun PreviewDonutGraph() {
    DonutChart(
        slices = listOf(
            DonutSlice(30f),
            DonutSlice(50f),
            DonutSlice(20f),
            DonutSlice(10f),
            DonutSlice(40f),
            DonutSlice(50f),
            DonutSlice(30f)  // More than 6 slices to see color reuse
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
