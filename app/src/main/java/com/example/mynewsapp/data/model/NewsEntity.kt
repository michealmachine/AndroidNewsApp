package com.example.mynewsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// 定义一个名为 "news" 的表，用于存储新闻数据
@Entity(tableName = "news")
data class NewsEntity(
    // URL 作为主键，每条新闻的 URL 都是唯一的
    @PrimaryKey val url: String,
    // 新闻的标题
    val title: String,
    // 新闻的描述
    val description: String?,
    // 新闻的作者
    val author: String?,
    // 新闻来源的名称
    val sourceName: String,
    // 新闻图片的 URL
    val urlToImage: String?,
    // 新闻发布的时间
    val publishedAt: String?,
    // 新闻的内容
    val content: String?,
    // 新闻的国家
    val country: String,
    // 标记新闻是否被收藏
    var isFavorite: Boolean = false
)

// 定义一个名为 "favorites" 的表，用于存储收藏的新闻数据
@Entity(tableName = "favorites")
data class FavoriteNewsEntity(
    // URL 作为主键，每条新闻的 URL 都是唯一的
    @PrimaryKey val url: String,
    // 新闻的标题
    val title: String,
    // 新闻的描述
    val description: String?,
    // 新闻的作者
    val author: String?,
    // 新闻来源的名称
    val sourceName: String,
    // 新闻图片的 URL
    val urlToImage: String?,
    // 新闻发布的时间
    val publishedAt: String?,
    // 新闻的内容
    val content: String?
)
