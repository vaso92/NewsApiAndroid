package com.example.newsapiandroid.domain.repository

import androidx.paging.PagingData
import com.example.newsapiandroid.data.paging.SortBy
import com.example.newsapiandroid.data.remote.dto.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun setSearchKeywords(value: String)
    val getSearchKeywords: Flow<String>

    suspend fun setSortBy(sortBy: SortBy)
    val getSortBy: Flow<SortBy>

    fun getNews(keywords: String, sortBy: SortBy, pageSize: Int): Flow<PagingData<Article>>

    suspend fun saveArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getArticle(url: String): Flow<Article?>

    fun getSavedArticles(): Flow<PagingData<Article>>
}