package com.example.mynewsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynewsapp.data.model.NewsEntity

@Database(entities = [NewsEntity::class], version =3,exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}