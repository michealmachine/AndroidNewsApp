package com.example.mynewsapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(context: Context) {

    private val dataStore = context.dataStore

    val isGridFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_GRID] ?: false
    }

    suspend fun saveIsGrid(isGrid: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_GRID] = isGrid
        }
    }

    private object PreferencesKeys {
        val IS_GRID = booleanPreferencesKey("is_grid")
    }
}
