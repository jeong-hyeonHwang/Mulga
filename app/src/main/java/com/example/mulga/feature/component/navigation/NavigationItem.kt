package com.example.mulga.feature.component.navigation

import androidx.annotation.StringRes
import com.example.mulga.R

sealed class NavigationItem(
    val route: String,
    @StringRes val titleId: Int,
    val iconRes: Int
) {
    object Home : NavigationItem("home", R.string.nav_title_home, R.drawable.ic_nav_home)
    object Calendar : NavigationItem("calendar", R.string.nav_title_calendar, R.drawable.ic_nav_calendar)
    object Analytics : NavigationItem("analytics", R.string.nav_title_analytics, R.drawable.ic_nav_analysis)
    object Profile : NavigationItem("profile", R.string.nav_title_profile, R.drawable.ic_nav_mypage)
}

