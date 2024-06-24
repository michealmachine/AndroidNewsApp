package com.example.mynewsapp.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mynewsapp.ui.NewsViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity

// 使用 ExperimentalMaterial3Api 注解，表示我们正在使用实验性的 Material 3 API
@OptIn(ExperimentalMaterial3Api::class)
// 定义一个 Composable 函数，用于显示收藏的新闻
@Composable
fun FavoriteNewsScreen(navController: NavHostController, viewModel: NewsViewModel = hiltViewModel()) {
    // 从 ViewModel 中获取 viewState 和 favorites
    val viewState by viewModel.viewState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    // 使用 Scaffold 创建一个带有顶部栏的布局
    Scaffold(
        topBar = {
            // 顶部栏包含一个标题和一个图标按钮
            TopAppBar(
                title = { Text("Favorite News") },
                actions = {
                    // 当图标按钮被点击时，切换显示格式
                    IconButton(onClick = { viewModel.toggleDisplayFormat() }) {
                        Icon(
                            // 根据 viewState.isGrid 的值，选择不同的图标
                            imageVector = if (viewState.isGrid) Icons.Default.List else Icons.Default.GridOn,
                            contentDescription = "Toggle Layout"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // 创建一个列布局，用于显示新闻列表或者一个提示文本
        Column(modifier = Modifier.padding(paddingValues)) {
            // 如果没有收藏的新闻，显示一个提示文本
            if (favorites.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No favorites yet")
                }
            } else {
                // 如果有收藏的新闻，根据 viewState.isGrid 的值，选择不同的列表布局
                if (viewState.isGrid) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favorites) { favorite ->
                            NewsItem(navController, favorite.toNewsEntity(), viewModel)
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(favorites) { favorite ->
                            NewsItem(navController, favorite.toNewsEntity(), viewModel)
                        }
                    }
                }
            }
        }
    }
}

// 定义一个扩展函数，用于将 FavoriteNewsEntity 对象转换为 NewsEntity 对象
fun FavoriteNewsEntity.toNewsEntity(): NewsEntity {
    return NewsEntity(
        url = this.url,
        title = this.title,
        description = this.description,
        author = this.author,
        sourceName = this.sourceName,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content,
        country = "", // 你需要提供一个默认值或从其他地方获取
    )
}

