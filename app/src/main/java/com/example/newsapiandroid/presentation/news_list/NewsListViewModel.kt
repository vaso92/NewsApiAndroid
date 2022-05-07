package com.example.newsapiandroid.presentation.news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newsapiandroid.data.paging.SortBy
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.di.DbDispatcher
import com.example.newsapiandroid.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @DbDispatcher private val dbDispatcher: CoroutineDispatcher
) : ViewModel() {
    private var currentKeywords: String? = null
    private var currentSortBy: SortBy? = null
    private var currentSearchResult: Flow<PagingData<Article>>? = null

    val searchKeywords = newsRepository.getSearchKeywords
    val sortBy = newsRepository.getSortBy

    fun setSortBy(sortBy: SortBy) {
        viewModelScope.launch {
            newsRepository.setSortBy(sortBy)
        }
    }

    fun news(keywords: String, sortBy: SortBy): Flow<PagingData<Article>> {
        val lastResult = currentSearchResult

        if (lastResult != null && keywords == currentKeywords && sortBy == currentSortBy) {
            return lastResult
        }

        currentKeywords = keywords
        currentSortBy = sortBy

        val newResult = newsRepository.getNews(keywords = keywords, sortBy = sortBy, pageSize = 20)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle

    fun onArticleSelected(article: Article?) {
        _selectedArticle.value = article
    }

    val isBookmarked = selectedArticle.flatMapLatest { article ->
        if (article != null) {
            newsRepository.getArticle(article.url)
                .transform {
                    it?.let { emit(true) } ?: emit(false)
                }
        } else {
            flow {  }
        }
    }
        .flowOn(dbDispatcher)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun onBookmarkPressed() {
        viewModelScope.launch(dbDispatcher) {
            selectedArticle.value?.let { article ->
                if (isBookmarked.value) {
                    newsRepository.deleteArticle(article)
                } else {
                    newsRepository.saveArticle(article)
                }
            }
        }
    }
}