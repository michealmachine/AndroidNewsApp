package com.example.mynewsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.remote.NewsApiService
import com.example.mynewsapp.data.repository.NewsRepository
import com.example.mynewsapp.ui.AppNavigation
import com.example.mynewsapp.ui.NewsViewModel
import com.example.mynewsapp.ui.screen.NewsDetailScreen
import com.example.mynewsapp.ui.screen.NewsScreen
import com.example.mynewsapp.ui.theme.MyNewsAppTheme
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

// 使用 AndroidEntryPoint 注解，表示这个 Activity 是 Hilt 的入口点
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // 当 Activity 创建时调用此方法
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置 Activity 的内容
        setContent {
            // 使用自定义的主题
            MyNewsAppTheme {
                // 通过 Hilt 获取 ViewModel 的实例
                val viewModel: NewsViewModel = hiltViewModel()
                // 设置导航
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}


