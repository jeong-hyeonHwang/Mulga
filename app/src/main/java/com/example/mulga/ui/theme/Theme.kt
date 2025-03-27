package com.example.mulga.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun MulGaTheme(
    content: @Composable () -> Unit
) {
    val mulGaColors = defaultMulGaColors
    val mulGaTypography = defaultMulGaTypography

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            background = Color.White
        ),
        typography = MaterialTheme.typography,
        content = {
            CompositionLocalProvider(
                LocalMulGaColors provides mulGaColors,
                LocalMulGaTypography provides mulGaTypography
            ) {
                content()
            }
        }
    )
}