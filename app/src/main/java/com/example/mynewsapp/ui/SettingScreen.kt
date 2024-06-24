package com.example.mynewsapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

// 使用 ExperimentalMaterial3Api 注解，因为我们正在使用一些实验性的 Material 3 组件
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(viewModel: NewsViewModel = hiltViewModel()) {
    // 使用 remember { mutableStateOf(false) } 创建一个可观察的状态，用于控制 AlertDialog 的显示和隐藏
    var showDialog by remember { mutableStateOf(false) }
    // 使用 rememberCoroutineScope() 创建一个 CoroutineScope，用于启动协程
    val scope = rememberCoroutineScope()

    // 使用 Scaffold 创建一个基本的布局结构，包括顶部的 AppBar 和内容区域
    Scaffold(
        topBar = {
            // 在顶部 AppBar 中显示标题
            TopAppBar(
                title = { Text("Settings") }
            )
        },
    ) { paddingValues ->
        // 在内容区域中显示一个列布局
        Column(modifier = Modifier.padding(paddingValues)) {
            // 在列布局中添加一个 Box，用于显示 "Clear Cache" 的按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Box 占满父布局的宽度
                    .height(56.dp) // 设置 Box 的高度为 56dp
                    .clickable { // 当 Box 被点击时执行以下操作
                        viewModel.clearCache() // 清除缓存
                        showDialog = true // 显示 AlertDialog
                        scope.launch { // 启动一个协程
                            kotlinx.coroutines.delay(3000) // 延迟 3 秒
                            showDialog = false // 隐藏 AlertDialog
                        }
                    }
                    .background(Color.LightGray), // 设置 Box 的背景颜色为浅灰色
                contentAlignment = Alignment.CenterStart // 设置 Box 中的内容在水平方向上靠左对齐，在垂直方向上居中对齐
            ) {
                // 在 Box 中显示一个文本，文本的内容为 "Clear Cache"
                Text(
                    "Clear Cache",
                    style = MaterialTheme.typography.bodyLarge, // 设置文本的样式
                    modifier = Modifier.padding(16.dp) // 设置文本的内边距为 16dp
                )
            }

            // 当 showDialog 为 true 时，显示一个 AlertDialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false }, // 当 AlertDialog 被取消时，将 showDialog 设置为 false
                    title = { Text("Notification") }, // 设置 AlertDialog 的标题为 "Notification"
                    text = { Text("Cache cleared") }, // 设置 AlertDialog 的内容为 "Cache cleared"
                    confirmButton = { // 设置 AlertDialog 的确认按钮
                        TextButton(onClick = { showDialog = false }) { // 当确认按钮被点击时，将 showDialog 设置为 false
                            Text("OK") // 设置确认按钮的文本为 "OK"
                        }
                    }
                )
            }
        }
    }
}