package com.example.newsapiandroid.data.repository

import com.example.newsapiandroid.data.remote.NewsApi
import com.example.newsapiandroid.data.remote.dto.AllNews
import com.example.newsapiandroid.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {
    override suspend fun getNews(keywords: String): AllNews {
        return newsApi.getNews(keywords = keywords, pageSize = 20, page = 1)
    }
}