package com.example.mynewsapp.data.repository

import android.util.Log
import com.example.mynewsapp.data.local.FavoriteNewsDao
import com.example.mynewsapp.data.local.NewsDao
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.remote.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao,
    private val favoriteNewsDao: FavoriteNewsDao
) {
    fun getNews(country: String): Flow<List<NewsEntity>> {
        return newsDao.getNewsDirectly(country)
    }

    suspend fun refreshNews(country: String): List<NewsEntity> {
        var newsEntities = listOf<NewsEntity>()
        withContext(Dispatchers.IO) {
            try {
                val response = newsApiService.getTopHeadlines(country, "2eaa769a0696417d889a734041735560")
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
                newsDao.insertNews(newsEntities)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Failed to refresh news", e)
            }
        }
        return newsEntities
    }

    fun getFavoriteNews(): Flow<List<FavoriteNewsEntity>> {
        return favoriteNewsDao.getFavoriteNews()
    }

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

    fun isFavorite(url: String): Flow<Boolean> {
        return favoriteNewsDao.isFavorite(url)
    }

    suspend fun clearAllNews() {
        newsDao.clearAllNews()
    }
}


