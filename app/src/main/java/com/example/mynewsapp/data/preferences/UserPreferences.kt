package com.example.mynewsapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 使用 preferencesDataStore 扩展函数创建一个 DataStore 实例
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {

    // 从 Context 获取 DataStore 实例
    private val dataStore = context.dataStore

    // 创建一个 Flow，用于观察 "is_grid" 配置项的变化
    // 如果 "is_grid" 配置项不存在，则返回 false
    val isGridFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_GRID] ?: false
    }

    // 将 "is_grid" 配置项的值保存到 DataStore 中
    suspend fun saveIsGrid(isGrid: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_GRID] = isGrid
        }
    }

    // 定义一个对象，用于存储配置项的 key
    private object PreferencesKeys {
        // 创建一个 Boolean 类型的配置项 key，名为 "is_grid"
        val IS_GRID = booleanPreferencesKey("is_grid")
    }
}
