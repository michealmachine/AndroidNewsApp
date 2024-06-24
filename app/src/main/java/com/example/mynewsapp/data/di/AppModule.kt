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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// 定义一个 Dagger 模块，用于提供应用程序所需的所有依赖项
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // 提供 Gson 实例
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    // 提供 Retrofit 实例
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

    // 提供 NewsApiService 实例
    @Provides
    @Singleton
    fun provideNewsApiService(retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    // 提供 NewsDatabase 实例
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext app: Context): NewsDatabase {
        return Room.databaseBuilder(app, NewsDatabase::class.java, "news_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // 提供 NewsDao 实例
    @Provides
    fun provideNewsDao(database: NewsDatabase) = database.newsDao()

    // 提供 FavoriteNewsDao 实例
    @Provides
    fun provideFavoriteNewsDao(database: NewsDatabase) = database.favoriteNewsDao()

    // 提供 UserPreferences 实例
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}