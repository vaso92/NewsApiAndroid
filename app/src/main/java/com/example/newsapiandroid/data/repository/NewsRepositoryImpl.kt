package com.example.newsapiandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsapiandroid.data.paging.NewsPagingSource
import com.example.newsapiandroid.data.remote.NewsApi
import com.example.newsapiandroid.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val newsApi: NewsApi) : NewsRepository {
    override fun getNews(keywords: String, pageSize: Int) = Pager(
        pagingSourceFactory = { NewsPagingSource(newsApi = newsApi, keywords = keywords, pageSize = pageSize) },
        config = PagingConfig(
            pageSize = 20
        )
    ).flow
}