package com.example.newsapiandroid.presentation.article_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapiandroid.di.DbDispatcher
import com.example.newsapiandroid.domain.repository.NewsRepository
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val newsRepository: NewsRepository,
    @DbDispatcher private val dbDispatcher: CoroutineDispatcher
) : ViewModel() {
    val isBookmarked =
        newsRepository.getArticle(ArticleDetailDestination.argsFrom(savedStateHandle).article.url)
            .transform { article ->
                article?.let { emit(true) } ?: emit(false)
            }
            .flowOn(dbDispatcher)
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = false
            )

    fun onBookmarkPressed() {
        viewModelScope.launch(dbDispatcher) {
            if (isBookmarked.value) {
                newsRepository.deleteArticle(ArticleDetailDestination.argsFrom(savedStateHandle).article)
            } else {
                newsRepository.saveArticle(ArticleDetailDestination.argsFrom(savedStateHandle).article)
            }
        }
    }
}