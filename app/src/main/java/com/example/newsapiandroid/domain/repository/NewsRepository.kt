package com.example.newsapiandroid.domain.repository

import androidx.paging.PagingData
import com.example.newsapiandroid.data.remote.dto.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNews(keywords: String, pageSize: Int): Flow<PagingData<Article>>
}