package com.ilm.mulga.feature.component.main

import TransactionDetailScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ilm.mulga.feature.analysis.AnalysisScreen
import com.ilm.mulga.feature.calendar.CalendarScreen
import com.ilm.mulga.feature.component.navigation.BottomNavigationBar
import com.ilm.mulga.feature.component.navigation.NavigationItem
import com.ilm.mulga.feature.home.HomeScreen
import com.ilm.mulga.feature.mypage.MypageScreen
import com.ilm.mulga.feature.transaction_detail.TransactionAddScreen
import com.ilm.mulga.feature.transaction_detail.TransactionDetailViewModel
import com.ilm.mulga.presentation.model.TransactionDetailData
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun MainScreen(
    onNavigateToTransactionAdd: () -> Unit,
    rootNavController: NavController
) {
    val tabNavController = rememberNavController()

    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // "transaction_add"와 "transaction_detail" 경로에서는 BottomNavigationBar를 표시하지 않습니다.
            if (currentRoute !in listOf("transaction_add", "transaction_detail")) {
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
            composable(NavigationItem.Analytics.route) { AnalysisScreen() }
            composable(NavigationItem.Profile.route) { MypageScreen() }
        }
    }
}
