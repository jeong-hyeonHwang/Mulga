package com.example.mulga.feature.analysis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mulga.ui.theme.MulGaTheme

@Composable
fun Separator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()  // Makes the Box fill the full width of the parent
            .height(16.dp)    // Set a fixed height for the separator (you can adjust this)
            .background(MulGaTheme.colors.grey5)  // Set the color of the separator
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSeparator() {
    Separator()
}
