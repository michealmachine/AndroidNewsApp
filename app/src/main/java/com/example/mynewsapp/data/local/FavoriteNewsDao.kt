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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(news: FavoriteNewsEntity)

    @Delete
    suspend fun deleteFavorite(news: FavoriteNewsEntity)

    @Query("SELECT * FROM favorites")
    fun getFavoriteNews(): Flow<List<FavoriteNewsEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE url = :url)")
    fun isFavorite(url: String):Flow<Boolean>
}