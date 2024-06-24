package com.example.mynewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity

// 定义一个 Room 数据库，包含 NewsEntity 和 FavoriteNewsEntity 两个实体
// 数据库版本为 4，exportSchema 为 false 表示不导出数据库的 schema
@Database(entities = [NewsEntity::class, FavoriteNewsEntity::class], version =4,exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    // 定义一个抽象方法，返回 NewsDao
    // Room 会自动生成这个方法的实现
    abstract fun newsDao(): NewsDao

    // 定义一个抽象方法，返回 FavoriteNewsDao
    // Room 会自动生成这个方法的实现
    abstract fun favoriteNewsDao(): FavoriteNewsDao
}