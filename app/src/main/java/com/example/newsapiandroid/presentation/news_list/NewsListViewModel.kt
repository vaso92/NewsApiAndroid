package com.example.newsapiandroid.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) :
    ViewModel() {
    private var currentKeywords: String = "sports"
    private var currentSearchResult: Flow<PagingData<Article>>? = null

    val searchKeywords = newsRepository.getSearchKeywords

    fun news(keywords: String?): Flow<PagingData<Article>> {
        val lastResult = currentSearchResult

        if (lastResult != null && keywords == currentKeywords) {
            return lastResult
        }

        if (keywords != null) {
            currentKeywords = keywords
        }
        val newResult = newsRepository.getNews(keywords = currentKeywords, pageSize = 20)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}