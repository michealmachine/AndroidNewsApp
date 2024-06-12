package com.example.mynewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity

@Database(entities = [NewsEntity::class, FavoriteNewsEntity::class], version =4,exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun favoriteNewsDao(): FavoriteNewsDao
}