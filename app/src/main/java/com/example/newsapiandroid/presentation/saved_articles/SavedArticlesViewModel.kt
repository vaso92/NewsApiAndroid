package com.example.newsapiandroid.presentation.saved_articles

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SavedArticlesViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    fun savedArticles(): Flow<PagingData<Article>> = newsRepository.getSavedArticles()
}