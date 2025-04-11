package com.ilm.mulga.domain.model

import android.graphics.drawable.Drawable

data class AppInfoEntity (
    val packageName: String,
    val appName: String,
    val appIcon: Drawable
)