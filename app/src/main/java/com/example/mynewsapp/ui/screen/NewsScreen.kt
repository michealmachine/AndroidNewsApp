package com.example.mynewsapp.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.ui.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavHostController, viewModel: NewsViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.newsUpdateCount.collectLatest { count ->
            snackbarHostState.showSnackbar("Updated with $count new articles")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News") },
                actions = {
                    IconButton(onClick = { viewModel.refreshNews() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { viewState.isGrid }) {
                        Icon(
                            imageVector = if (viewState.isGrid) Icons.Default.List else Icons.Default.GridOn,
                            contentDescription = "Toggle Layout",
                            modifier = Modifier.clickable {
                                viewModel.toggleDisplayFormat()
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState,
            snackbar = { data -> CustomSnackbar(data) })}
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                value = viewState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            if (viewState.isGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewState.newsList) { article ->
                        NewsItem(navController, article, viewModel)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(viewState.newsList) { article ->
                        NewsItem(navController, article, viewModel)
                    }
                }
            }
        }
    }
}
@Composable
fun CustomSnackbar(data: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        contentColor = Color.Black // 设置文字颜色
    ) {
        Text(text = data.visuals.message)
    }
}
@Composable
fun NewsItem(navController: NavHostController, article: NewsEntity, viewModel: NewsViewModel) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(article.urlToImage)
            .crossfade(true)
            .build(),
        onError = {
            isLoading = false
            hasError = true
        },
        onSuccess = {
            isLoading = false
            hasError = false
        }
    )
    val alpha by animateFloatAsState(targetValue = if (isLoading) 0.5f else 1f)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                viewModel.selectNews(article)
                navController.navigate("newsDetail")
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.height(200.dp)) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha)
                )
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
                if (hasError) {
                    Text(
                        text = "Image load failed",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Text(
                text = article.title,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
