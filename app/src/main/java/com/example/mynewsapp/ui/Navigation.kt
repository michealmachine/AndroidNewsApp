package com.example.mynewsapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mynewsapp.ui.screen.NewsDetailScreen
import com.example.mynewsapp.ui.screen.NewsScreen
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mynewsapp.ui.screen.FavoriteNewsScreen

@Composable
fun AppNavigation(viewModel: NewsViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationItems = listOf(
        NavigationBarItemModel("news", Icons.Default.Home, "News"),
        NavigationBarItemModel("favorites", Icons.Default.Favorite, "Favorites"),
        NavigationBarItemModel("settings", Icons.Default.Settings, "Settings")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "news") {
            composable("news") {
                NewsScreen(navController, viewModel)
            }
            composable("newsDetail") {
                NewsDetailScreen(navController, viewModel)
            }
            composable("favorites") {
                FavoriteNewsScreen(navController,viewModel)
            }
            composable("settings") {
                SettingScreen(viewModel)
            }
        }
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            NavigationBar {
                navigationItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route)
                        }
                    )
                }
            }
        }
    }
}

data class NavigationBarItemModel(
    val route: String,
    val icon: ImageVector,
    val label: String
)