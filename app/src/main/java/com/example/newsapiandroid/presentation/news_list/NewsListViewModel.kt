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
class NewsListViewModel @Inject constructor(private val newsRepository: NewsRepository) :
    ViewModel() {
    private var currentKeywords: String = "sports"

    fun news(): Flow<PagingData<Article>> {
        return newsRepository.getNews(keywords = currentKeywords, pageSize = 20)
            .cachedIn(viewModelScope)
    }
}