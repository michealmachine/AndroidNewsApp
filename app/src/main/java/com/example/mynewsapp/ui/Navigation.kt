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

// 定义一个 Composable 函数，用于管理应用的导航
@Composable
fun AppNavigation(viewModel: NewsViewModel) {
    // 创建一个 NavController 实例，用于管理导航
    val navController = rememberNavController()
    // 获取当前的导航堆栈条目
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    // 获取当前的路由
    val currentRoute = navBackStackEntry?.destination?.route

    // 定义一个列表，包含所有的导航项
    val navigationItems = listOf(
        NavigationBarItemModel("news", Icons.Default.Home, "News"),
        NavigationBarItemModel("favorites", Icons.Default.Favorite, "Favorites"),
        NavigationBarItemModel("settings", Icons.Default.Settings, "Settings")
    )

    // 创建一个 Box 布局，用于包含 NavHost 和 Column
    Box(modifier = Modifier.fillMaxSize()) {
        // 创建一个 NavHost，用于管理导航
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
        // 创建一个 Column 布局，用于显示导航栏
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

// 定义一个数据类，用于表示导航项
data class NavigationBarItemModel(
    val route: String,  // 导航项的路由
    val icon: ImageVector,  // 导航项的图标
    val label: String  // 导航项的标签
)