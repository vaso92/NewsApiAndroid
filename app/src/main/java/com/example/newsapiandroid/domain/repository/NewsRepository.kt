package com.example.newsapiandroid.domain.repository

import androidx.paging.PagingData
import com.example.newsapiandroid.data.db.entity.ArticleEntity
import com.example.newsapiandroid.data.remote.dto.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun setSearchKeywords(value: String)
    val getSearchKeywords: Flow<String?>

    fun getNews(keywords: String, pageSize: Int): Flow<PagingData<Article>>

    suspend fun saveArticle(article: Article)

    suspend fun getSavedArticles(): Flow<List<Article>>
}