package com.example.mynewsapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.mynewsapp.data.local.NewsDatabase
import com.example.mynewsapp.data.preferences.UserPreferences
import com.example.mynewsapp.data.remote.NewsApiService
import dagger.Module
import dagger.Provides
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                    .addInterceptor(loggingInterceptor) // 添加日志拦截器
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): NewsDatabase {
        return Room.databaseBuilder(app, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideNewsDao(database: NewsDatabase) = database.newsDao()
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}