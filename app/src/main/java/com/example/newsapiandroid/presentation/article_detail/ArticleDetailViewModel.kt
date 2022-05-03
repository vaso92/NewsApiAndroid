package com.example.newsapiandroid.presentation.article_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapiandroid.domain.repository.NewsRepository
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository
) : ViewModel() {
    fun onBookmarkPressed() {
        viewModelScope.launch {
            newsRepository.saveArticle(ArticleDetailDestination.argsFrom(savedStateHandle).article)
        }
    }
}