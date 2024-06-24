package com.example.mynewsapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.preferences.UserPreferences
import com.example.mynewsapp.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// 定义一个数据类，用于表示新闻视图的状态
data class NewsViewState(
    val newsList: List<NewsEntity> = emptyList(),  // 新闻列表
    val searchQuery: String = "",  // 搜索查询
    val isGrid: Boolean = false,  // 是否以网格形式显示新闻
    val selectedCountry: String = "us",  // 选中的国家
    val isLoading: Boolean = false  // 是否正在加载新闻
)

// 使用 HiltViewModel 注解，表示这是一个由 Hilt 管理的 ViewModel
@HiltViewModel
class NewsViewModel @Inject constructor(  // 使用 Inject 注解，表示这个类的实例需要通过依赖注入来创建
    private val repository: NewsRepository,  // 新闻仓库，用于获取新闻数据
) : ViewModel() {  // 继承 ViewModel 类，表示这是一个 ViewModel

    // 定义一个 MutableStateFlow 实例，用于管理新闻视图的状态
    private val _viewState = MutableStateFlow(NewsViewState())
    // 定义一个 StateFlow 实例，用于观察新闻视图的状态
    val viewState: StateFlow<NewsViewState> = _viewState.asStateFlow()

    // 定义一个 MutableStateFlow 实例，用于管理选中的新闻
    private val _selectedNews = MutableStateFlow<NewsEntity?>(null)
    // 定义一个 StateFlow 实例，用于观察选中的新闻
    val selectedNews: StateFlow<NewsEntity?> = _selectedNews

    // 定义一个 MutableSharedFlow 实例，用于管理新闻更新的数量
    private val _newsUpdateCount = MutableSharedFlow<Int>()
    // 定义一个 SharedFlow 实例，用于观察新闻更新的数量
    val newsUpdateCount: SharedFlow<Int> = _newsUpdateCount.asSharedFlow()

    // 定义一个 StateFlow 实例，用于观察选中的新闻是否被收藏
    val selectedNewsIsFavorite: StateFlow<Boolean> = _selectedNews.flatMapLatest { news ->
        news?.let { repository.isFavorite(it.url) } ?: flowOf(false)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    // 定义一个 StateFlow 实例，用于观察收藏的新闻列表
    val favorites: StateFlow<List<FavoriteNewsEntity>> = repository.getFavoriteNews()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // 在初始化时，加载用户偏好设置和新闻
    init {
        loadUserPreferences()
        loadNews(_viewState.value.selectedCountry)
    }

    // 定义一个函数，用于加载新闻
    private fun loadNews(country: String) {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            repository.getNews(country)
                .collect { news ->
                    val filteredNews = news.filter {
                        it.title.contains(_viewState.value.searchQuery, ignoreCase = true) ||
                                it.description?.contains(
                                    _viewState.value.searchQuery,
                                    ignoreCase = true
                                ) == true
                    }
                    _viewState.update { it.copy(newsList = filteredNews, isLoading = false) }
                }
        }
    }

    // 定义一个函数，用于加载用户偏好设置
    private fun loadUserPreferences() {
        viewModelScope.launch {
            repository.loadUserPreferences().collect { isGrid ->
                _viewState.update { it.copy(isGrid = isGrid) }
            }
        }
    }

    // 定义一个函数，用于刷新新闻
    fun refreshNews() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            val newNewsList = repository.refreshNews(_viewState.value.selectedCountry)
            _viewState.update { it.copy(isLoading = false) }
            _newsUpdateCount.emit(newNewsList.size)
        }
    }

    // 定义一个函数，用于更新搜索查询
    fun updateSearchQuery(query: String) {
        _viewState.update { it.copy(searchQuery = query) }
        loadNews(_viewState.value.selectedCountry)
    }

    // 定义一个函数，用于切换显示格式
    fun toggleDisplayFormat() {
        viewModelScope.launch {
            val newIsGrid = !_viewState.value.isGrid
            repository.changePreferences(newIsGrid)
            _viewState.update { it.copy(isGrid = newIsGrid) }
        }
    }

    // 定义一个函数，用于选择新闻
    fun selectNews(news: NewsEntity) {
        _selectedNews.value = news
    }

    // 定义一个函数，用于更新选中的国家
    fun updateSelectedCountry(country: String) {
        _viewState.update { it.copy(selectedCountry = country) }
        loadNews(country)
    }

    // 定义一个函数，用于切换收藏状态
    fun toggleFavorite(news: NewsEntity) {
        viewModelScope.launch {
            if (selectedNewsIsFavorite.value) {
                repository.removeFavorite(news)
            } else {
                repository.addFavorite(news)
            }
        }
    }

    // 定义一个函数，用于清空缓存
    fun clearCache() {
        viewModelScope.launch {
            repository.clearAllNews()
        }
    }
}