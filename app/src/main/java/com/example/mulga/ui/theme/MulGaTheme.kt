package com.ilm.mulga.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object MulGaTheme {
    val colors: MulGaColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMulGaColors.current

    val typography: MulGaTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalMulGaTypography.current
}
