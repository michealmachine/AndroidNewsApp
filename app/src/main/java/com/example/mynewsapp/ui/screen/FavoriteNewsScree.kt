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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteNewsScreen(navController: NavHostController, viewModel: NewsViewModel = hiltViewModel()) {
    val viewState by viewModel.viewState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite News") },
                actions = {
                    IconButton(onClick = { viewModel.toggleDisplayFormat() }) {
                        Icon(
                            imageVector = if (viewState.isGrid) Icons.Default.List else Icons.Default.GridOn,
                            contentDescription = "Toggle Layout"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (favorites.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No favorites yet")
                }
            } else {
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