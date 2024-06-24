package com.example.mynewsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteNewsDao {
    // 插入一个新的收藏新闻到数据库中
    // 如果已经存在相同的新闻（根据主键判断），则替换它
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(news: FavoriteNewsEntity)

    // 从数据库中删除一个收藏的新闻
    @Delete
    suspend fun deleteFavorite(news: FavoriteNewsEntity)

    // 获取所有收藏的新闻
    // 返回一个 Flow，可以用来观察数据的变化
    @Query("SELECT * FROM favorites")
    fun getFavoriteNews(): Flow<List<FavoriteNewsEntity>>

    // 检查一个新闻（通过 URL）是否已经被收藏
    // 返回一个 Flow，可以用来观察数据的变化
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE url = :url)")
    fun isFavorite(url: String):Flow<Boolean>
}