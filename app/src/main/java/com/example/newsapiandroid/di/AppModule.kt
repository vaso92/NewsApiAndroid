package com.example.newsapiandroid.di

import android.content.Context
import androidx.room.Room
import com.example.newsapiandroid.common.Constants
import com.example.newsapiandroid.data.db.NewsDao
import com.example.newsapiandroid.data.db.NewsDatabase
import com.example.newsapiandroid.data.remote.NewsApi
import com.example.newsapiandroid.data.repository.NewsRepositoryImpl
import com.example.newsapiandroid.domain.repository.NewsRepository
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import xin.sparkle.moshi.NullSafeKotlinJsonAdapterFactory
import xin.sparkle.moshi.NullSafeStandardJsonAdapters
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(Interceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer a396ce9b8613411ca3ce2433cd10671f")
                    .build()
                chain.proceed(newRequest)
            })
            .build()

        val nullSafeMoshi = Moshi.Builder()
            .add(NullSafeStandardJsonAdapters.FACTORY)
            .add(NullSafeKotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(nullSafeMoshi))
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsApi,
        newsDao: NewsDao,
        @ApplicationContext context: Context
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi = newsApi, newsDao = newsDao, context = context)
    }

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java, "news-database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao {
        return newsDatabase.newsDao()
    }
}
