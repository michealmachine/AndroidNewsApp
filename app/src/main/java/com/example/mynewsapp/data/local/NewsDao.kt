package com.example.mynewsapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mynewsapp.data.model.FavoriteNewsEntity
import com.example.mynewsapp.data.model.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    // 从数据库中获取指定国家的新闻
    // 返回一个 Flow，可以用来观察数据的变化
    @Query("SELECT * FROM news WHERE country = :country")
    fun getNewsDirectly(country: String): Flow<List<NewsEntity>>

    // 将一组新闻插入到数据库中
    // 如果已经存在相同的新闻（根据主键判断），则替换它
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    // 从数据库中删除所有新闻
    @Query("DELETE FROM news")
    suspend fun clearAllNews()
}