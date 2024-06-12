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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(viewModel: NewsViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState, snackbar = { data -> CustomSnackbar(data) }) },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // 设置高度与 TopAppBar 相同
                    .clickable {
                        viewModel.clearCache()
                        scope.launch {
                            delay(500) // delay to simulate the clearing cache process
                            snackbarHostState.showSnackbar("Cache cleared")
                        }
                    }
                    .background(Color.LightGray), // 设置背景颜色为灰色
                contentAlignment = Alignment.CenterStart
            ) {
                Text("Clear Cache", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(16.dp))
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