package com.ilm.mulga.feature.component.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ilm.mulga.feature.analysis.AnalysisDetailScreen
import com.ilm.mulga.feature.analysis.AnalysisScreen
import com.ilm.mulga.feature.analysis.AnalysisViewModel
import com.ilm.mulga.feature.calendar.CalendarScreen
import com.ilm.mulga.feature.component.navigation.BottomNavigationBar
import com.ilm.mulga.feature.component.navigation.NavigationItem
import com.ilm.mulga.feature.home.HomeScreen
import com.ilm.mulga.feature.mypage.MypageScreen


@Composable
fun MainScreen(
    onNavigateToTransactionAdd: () -> Unit,
    rootNavController: NavController
) {
    val tabNavController = rememberNavController()

    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val viewModel: AnalysisViewModel = viewModel()

    Scaffold(
        bottomBar = {
            // "transaction_add"와 "transaction_detail" 경로에서는 BottomNavigationBar를 표시하지 않습니다.
            if (currentRoute !in listOf("transaction_add", "transaction_detail", "analysisDetailScreen")) {
                BottomNavigationBar(navController = tabNavController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = NavigationItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItem.Home.route) { HomeScreen() }
            composable(NavigationItem.Calendar.route) {
                CalendarScreen(
                    onNavigateToTransactionAdd = onNavigateToTransactionAdd,
//                    navController = tabNavController
                )
            }
            composable(NavigationItem.Analytics.route) { AnalysisScreen(tabNavController, viewModel) }
            composable(NavigationItem.Profile.route) { MypageScreen() }
            composable("analysisDetailScreen") {
                AnalysisDetailScreen(viewModel) }
        }
    }
}
