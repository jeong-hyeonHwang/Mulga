package com.ilm.mulga.feature.local

import androidx.activity.ComponentActivity
import androidx.compose.runtime.compositionLocalOf

val LocalActivity = compositionLocalOf<ComponentActivity> {
    error("No Activity provided")
}
