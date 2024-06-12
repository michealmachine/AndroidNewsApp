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
    val isGrid: Boolean = false,
    val selectedCountry: String = "us",
    val isLoading: Boolean = false // 新增加载状态
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
        loadUserPreferences()
        loadNews(_viewState.value.selectedCountry)
    }

    private fun loadNews(country: String) {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) } // ���置加载状态为 true
            repository.getNews(country)
                .collect { news ->
                    val filteredNews = news.filter {
                        it.title.contains(_viewState.value.searchQuery, ignoreCase = true) ||
                                it.description?.contains(_viewState.value.searchQuery, ignoreCase = true) == true
                    }
                    _viewState.update { it.copy(newsList = filteredNews, isLoading = false) } // 设置加载状态为 false
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
            _viewState.update { it.copy(isLoading = true) } // 设置加载状态为 true
            val newNewsList = repository.refreshNews(_viewState.value.selectedCountry)
            _viewState.update { it.copy(isLoading = false) } // 设置加载状态为 false
            _newsUpdateCount.emit(newNewsList.size)
        }
    }

    fun updateSearchQuery(query: String) {
        _viewState.update { it.copy(searchQuery = query) }
        loadNews(_viewState.value.selectedCountry)
    }

    fun toggleDisplayFormat() {
        viewModelScope.launch {
            val newIsGrid = !_viewState.value.isGrid
            userPreferences.saveIsGrid(newIsGrid)
            _viewState.update { it.copy(isGrid = newIsGrid) }
        }
    }

    fun selectNews(news: NewsEntity) {
        _selectedNews.value = news
    }

    fun updateSelectedCountry(country: String) {
        _viewState.update { it.copy(selectedCountry = country) } // 清空旧的新闻数据

        loadNews(_viewState.value.selectedCountry)

    }
}
