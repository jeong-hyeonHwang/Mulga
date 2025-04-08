package com.ilm.mulga.feature.component.main

import TransactionDetailScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.ilm.mulga.feature.transaction_detail.TransactionDetailViewModel
import com.ilm.mulga.presentation.model.TransactionDetailData
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun MainScreen(
    onNavigateToTransactionAdd: () -> Unit
) {
    val tabNavController = rememberNavController()

    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("transaction_add")) {
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
            composable(NavigationItem.Calendar.route) { CalendarScreen(navController = tabNavController) }
            composable(NavigationItem.Calendar.route) {
                CalendarScreen(onNavigateToTransactionAdd = onNavigateToTransactionAdd, navController = tabNavController)
            }
            composable(NavigationItem.Analytics.route) { AnalysisScreen() }
            composable(NavigationItem.Profile.route) { MypageScreen() }
            composable(
                route = "transaction_detail?data={data}",
                arguments = listOf(navArgument("data") { type = NavType.StringType })
            ) { backStackEntry ->
                val dataParam = backStackEntry.arguments?.getString("data")
                val transactionDetailData = dataParam?.let {
                    val decodedData = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                    Json.decodeFromString<TransactionDetailData>(decodedData)
                }
                // Koin을 사용하는 경우, koinViewModel()으로 동일한 인스턴스를 받습니다.
                val viewModel: TransactionDetailViewModel = koinViewModel()
                if (transactionDetailData != null && viewModel.transactionDetailData.value == null) {
                    viewModel.setTransactionDetail(transactionDetailData)
                }
                TransactionDetailScreen(viewModel = viewModel, navController = tabNavController)
            }

        }
    }
}
