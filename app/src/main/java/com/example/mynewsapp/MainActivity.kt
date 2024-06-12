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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNewsAppTheme {
                val viewModel: NewsViewModel = hiltViewModel()
                AppNavigation(viewModel = viewModel)
            }

        }
    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyNewsAppTheme {
        Greeting("Android")
    }
}