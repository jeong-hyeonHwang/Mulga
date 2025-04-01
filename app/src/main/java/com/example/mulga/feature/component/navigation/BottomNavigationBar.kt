package com.example.mulga.feature.component.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarDefaults.containerColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mulga.ui.theme.LocalMulGaColors
import com.example.mulga.ui.theme.LocalMulGaTypography

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Calendar,
        NavigationItem.Analytics,
        NavigationItem.Profile
    )
    NavigationBar(
        containerColor = LocalMulGaColors.current.grey5
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                    ImageVector.vectorResource(id = item.iconRes),
                    contentDescription = stringResource(id = item.titleId),
                    modifier = Modifier.size(16.dp)
                    )},
                label = { Text(
                    text = stringResource(id = item.titleId),
                    style = LocalMulGaTypography.current.caption)},
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(

                    indicatorColor = LocalMulGaColors.current.grey4,
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}
