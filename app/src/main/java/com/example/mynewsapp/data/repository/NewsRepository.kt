package com.example.mynewsapp.data.repository

import android.util.Log
import com.example.mynewsapp.data.local.FavoriteNewsDao
import com.example.mynewsapp.data.local.NewsDao
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.preferences.UserPreferences
import com.example.mynewsapp.data.remote.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

// NewsRepository 类，用于管理新闻数据的获取和存储
class NewsRepository @Inject constructor(
    // 通过依赖注入获取 NewsApiService 实例
    private val newsApiService: NewsApiService,
    // 通过依赖注入获取 NewsDao 实例
    private val newsDao: NewsDao,
    // 通过依赖注入获取 FavoriteNewsDao 实例
    private val favoriteNewsDao: FavoriteNewsDao,

    private val userPreferences: UserPreferences
) {
    // 从数据库中获取指定国家的新闻
    fun getNews(country: String): Flow<List<NewsEntity>> {
        return newsDao.getNewsDirectly(country)
    }

    // 从网络获取指定国家的新闻，并保存到数据库中
    suspend fun refreshNews(country: String): List<NewsEntity> {
        var newsEntities = listOf<NewsEntity>()
        withContext(Dispatchers.IO) {
            try {
                // 从网络获取新闻
                val response = newsApiService.getTopHeadlines(country, "2eaa769a0696417d889a734041735560")
                // 将获】取的新闻转换为 NewsEntity 对象，并保存到列表中
                newsEntities = response.articles.map { article ->
                    NewsEntity(
                        url = article.url,
                        title = article.title,
                        description = article.description,
                        author = article.author,
                        sourceName = article.source.name,
                        urlToImage = article.urlToImage,
                        publishedAt = article.publishedAt,
                        content = article.content,
                        country = country
                    )
                }
                // 将新闻保存到数据库中
                newsDao.insertNews(newsEntities)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Failed to refresh news", e)
            }
        }
        return newsEntities
    }

    // 从数据库中获取收藏的新闻
    fun getFavoriteNews(): Flow<List<FavoriteNewsEntity>> {
        return favoriteNewsDao.getFavoriteNews()
    }

    // 将指定的新闻添加到收藏
    suspend fun addFavorite(news: NewsEntity) {
        val favoriteNews = FavoriteNewsEntity(
            url = news.url,
            title = news.title,
            description = news.description,
            author = news.author,
            sourceName = news.sourceName,
            urlToImage = news.urlToImage,
            publishedAt = news.publishedAt,
            content = news.content
        )
        favoriteNewsDao.insertFavorite(favoriteNews)
    }

    // 将指定的新闻从收藏中移除
    suspend fun removeFavorite(news: NewsEntity) {
        val favoriteNews = FavoriteNewsEntity(
            url = news.url,
            title = news.title,
            description = news.description,
            author = news.author,
            sourceName = news.sourceName,
            urlToImage = news.urlToImage,
            publishedAt = news.publishedAt,
            content = news.content
        )
        favoriteNewsDao.deleteFavorite(favoriteNews)
    }

    // 判断指定的新闻是否被收藏
    fun isFavorite(url: String): Flow<Boolean> {
        return favoriteNewsDao.isFavorite(url)
    }

    // 清空数据库中的所有新闻
    suspend fun clearAllNews() {
        newsDao.clearAllNews()
    }
    suspend fun loadUserPreferences(): Flow<Boolean> {
       return userPreferences.isGridFlow
    }

    suspend fun changePreferences(newIsGrid:Boolean){
        userPreferences.saveIsGrid(newIsGrid)
    }
}

