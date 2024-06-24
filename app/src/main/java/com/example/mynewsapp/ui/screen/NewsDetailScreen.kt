package com.example.mynewsapp.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.mynewsapp.ui.NewsViewModel
import androidx.compose.ui.platform.LocalContext

// 使用 ExperimentalMaterial3Api 注解，表示我们正在使用实验性的 Material 3 API
@OptIn(ExperimentalMaterial3Api::class)
// 定义一个 Composable 函数，用于显示新闻详情
@Composable
fun NewsDetailScreen(navController: NavHostController, viewModel: NewsViewModel) {
    // 从 ViewModel 中获取选中的新闻和收藏状态
    val selectedNews by viewModel.selectedNews.collectAsState()
    val context = LocalContext.current
    val isFavorite by viewModel.selectedNewsIsFavorite.collectAsState()

    // 使用 Scaffold 创建一个带有顶部栏的布局
    Scaffold(
        topBar = {
            // 顶部栏包含一个标题和两个图标按钮
            TopAppBar(
                title = { Text("News Detail") },
                navigationIcon = {
                    // 当图标按钮被点击时，返回上一个页面
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // 当图标按钮被点击时，切换收藏状态
                    IconButton(onClick = {
                        selectedNews?.let {
                            viewModel.toggleFavorite(it)
                        }
                    }) {
                        Icon(
                            // 根据收藏状态，选择不同的图标和描述
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // 如果选中了新闻，显示新闻详情
        if (selectedNews != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 使用 AsyncImagePainter 加载图片
                val painter = rememberAsyncImagePainter(selectedNews!!.urlToImage)
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                // 显示新闻的标题、作者、来源、发布时间、描述、内容和 URL
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedNews!!.title,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "By ${selectedNews!!.author ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Source: ${selectedNews!!.sourceName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Published at: ${selectedNews!!.publishedAt ?: "Unknown"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedNews!!.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedNews!!.content ?: "No content available",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "URL: ${selectedNews!!.url}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // 当 URL 被点击时，打开浏览器查看新闻
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedNews!!.url))
                            context.startActivity(intent)
                        }
                )
            }
        } else {
            // 如果没有选中新闻，显示一个提示文本
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No news selected")
            }
        }
    }
}

