package com.example.mulga.feature.component.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mulga.feature.analysis.AnalysisScreen
import com.example.mulga.feature.calendar.CalendarScreen
import com.example.mulga.feature.component.navigation.BottomNavigationBar
import com.example.mulga.feature.component.navigation.NavigationItem
import com.example.mulga.feature.home.HomeScreen
import com.example.mulga.feature.mypage.MypageScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Home.route) { HomeScreen() }
            composable(NavigationItem.Calendar.route) { CalendarScreen() }
            composable(NavigationItem.Analytics.route) { AnalysisScreen() }
            composable(NavigationItem.Profile.route) { MypageScreen() }
        }
    }
}
