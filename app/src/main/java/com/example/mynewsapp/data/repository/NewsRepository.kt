package com.example.mynewsapp.data.repository

import android.util.Log
import com.example.mynewsapp.data.local.NewsDao
import com.example.mynewsapp.data.model.NewsEntity
import com.example.mynewsapp.data.remote.NewsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDao: NewsDao
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
                        country = country // 设置国家
                    )
                }
                newsDao.insertNews(newsEntities)
            } catch (e: Exception) {
                Log.e("NewsRepository", "Failed to refresh news", e)
                // Handle error
            }
        }
        return newsEntities
    }
}
