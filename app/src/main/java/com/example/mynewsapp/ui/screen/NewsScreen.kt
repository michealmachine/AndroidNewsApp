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
import com.example.mynewsapp.ui.NewsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(navController: NavHostController, viewModel: NewsViewModel = hiltViewModel()) {
    // 从 ViewModel 中获取 viewState
    val viewState by viewModel.viewState.collectAsState()
    // 创建一个状态用于显示 Dialog
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    // 定义一个国家列表
    val countries = listOf("us", "gb", "fr", "cn", "jp")

    // 当 ViewModel 中的 newsUpdateCount 发生变化时，显示一个 Dialog
    LaunchedEffect(Unit) {
        viewModel.newsUpdateCount.collectLatest { count ->
            dialogMessage = "Updated with $count new articles"
            showDialog = true
        }
    }

    // 使用 Scaffold 创建一个带有顶部栏的布局
    Scaffold(
        topBar = {
            // 顶部栏包含一个标题和两个图标按钮
            TopAppBar(
                title = { Text("News") },
                actions = {
                    // 第一个图标按钮用于刷新新闻
                    IconButton(onClick = { viewModel.refreshNews() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    // 第二个图标按钮用于切换显示格式
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
        // 创建一个列布局，用于显示新闻列表或者一个加载指示器
        Column(modifier = Modifier.padding(paddingValues)) {
            // 创建一个下拉菜单，用于选择国家
            var expanded by remember { mutableStateOf(false) }
            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(text = viewState.selectedCountry.uppercase())
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(text = { Text(text = country.uppercase()) }, onClick = {
                            viewModel.updateSelectedCountry(country)
                            expanded = false
                        })
                    }
                }
            }

            // 创建一个文本输入框，用于搜索新闻
            TextField(
                value = viewState.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // 如果正在加载新闻，显示一个加载指示器
            if (viewState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // 如果已经加载完新闻，根据 viewState.isGrid 的值，选择不同的列表布局
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

        // 显示 Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Notification") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

// 定义一个 Composable 函数，用于显示新闻项
@Composable
fun NewsItem(navController: NavHostController, article: NewsEntity, viewModel: NewsViewModel) {
    // 创建两个状态，用于跟踪图片的加载状态
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    // 创建一个 AsyncImagePainter 实例，用于加载图片
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
    // 创建一个动画，用于改变图片的透明度
    val alpha by animateFloatAsState(targetValue = if (isLoading) 0.5f else 1f)

    // 创建一个卡片，用于显示新闻项
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
                // 如果正在加载图片，显示一个加载指示器
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
                // 如果加载图片失败，显示一个错误文本
                if (hasError) {
                    Text(
                        text = "Image load failed",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            // 显示新闻的标题
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


