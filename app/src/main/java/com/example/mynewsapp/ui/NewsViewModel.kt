package com.example.mynewsapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.preferences.UserPreferences
import com.example.mynewsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class NewsViewState(
    val newsList: List<NewsEntity> = emptyList(),
    val searchQuery: String = "",
    val isGrid: Boolean = false
)

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _viewState = MutableStateFlow(NewsViewState())
    val viewState: StateFlow<NewsViewState> = _viewState.asStateFlow()

    private val _selectedNews = MutableStateFlow<NewsEntity?>(null)
    val selectedNews: StateFlow<NewsEntity?> = _selectedNews

    private val _newsUpdateCount = MutableSharedFlow<Int>()
    val newsUpdateCount: SharedFlow<Int> = _newsUpdateCount.asSharedFlow()

    init {
        // 初始化时加载新闻和用户偏好
        loadNews()
        loadUserPreferences()
    }

    private fun loadNews() {
        viewModelScope.launch {
            repository.getNews()
                .combine(_viewState.map { it.searchQuery }) { news, query ->
                    news.filter { it.title.contains(query, ignoreCase = true) || it.description?.contains(query, ignoreCase = true) == true }
                }
                .collect { filteredNews ->
                    _viewState.update { it.copy(newsList = filteredNews) }
                }
        }
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            userPreferences.isGridFlow.collect { isGrid ->
                _viewState.update { it.copy(isGrid = isGrid) }
            }
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            val newNewsList = repository.refreshNews()
            _newsUpdateCount.emit(newNewsList.size)
            Log.d("te",newNewsList.size.toString())
        }
    }


    fun updateSearchQuery(query: String) {
        _viewState.update { it.copy(searchQuery = query) }
    }

    fun toggleDisplayFormat() {
        viewModelScope.launch {
            val newIsGrid = !_viewState.value.isGrid
            userPreferences.saveIsGrid(newIsGrid)
            _viewState.update { it.copy(isGrid = newIsGrid) }
        }
    }
    fun selectNews(news: NewsEntity) {
        Log.d("test","???")
        _selectedNews.value = news
        Log.d("ta",_selectedNews.value.toString())
    }
}
