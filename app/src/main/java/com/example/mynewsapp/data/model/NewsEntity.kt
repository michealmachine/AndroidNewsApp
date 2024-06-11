package com.example.mynewsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String?,
    val author: String?,
    val sourceName: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)