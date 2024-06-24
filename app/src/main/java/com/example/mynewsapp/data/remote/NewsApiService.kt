package com.example.mynewsapp.data.remote

import com.example.mynewsapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

// 定义一个接口，用于描述新闻 API 的网络请求
interface NewsApiService {
    // 定义一个 GET 请求，路径为 "v2/top-headlines"
    // 这个请求会获取指定国家的头条新闻
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        // "country" 查询参数，用于指定新闻的国家
        @Query("country") country: String,
        // "apiKey" 查询参数，用于验证 API 请求
        @Query("apiKey") apiKey: String
    ): NewsResponse  // 返回一个 NewsResponse 对象，包含新闻的状态、总数和列表
}