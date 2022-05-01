package com.example.newsapiandroid.presentation.news_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapiandroid.common.Resource
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.domain.use_case.get_news.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

sealed class NewsListState {
    object Loading : NewsListState()
    class Success(val news: List<Article>) : NewsListState()
    class Error(val error: String) : NewsListState()
}

@HiltViewModel
class NewsListViewModel @Inject constructor(private val getNewsUseCase: GetNewsUseCase) :
    ViewModel() {
    private val _state = mutableStateOf<NewsListState>(NewsListState.Loading)
    val state: State<NewsListState> = _state

    init {
        getNews()
    }

    private fun getNews() {
        getNewsUseCase().onEach { response ->
            when (response) {
                is Resource.Error -> _state.value =
                    NewsListState.Error(error = response.message ?: "An unexpected error occurred.")
                is Resource.Loading -> _state.value = NewsListState.Loading
                is Resource.Success -> _state.value =
                    NewsListState.Success(news = response.data ?: emptyList())
            }
        }.launchIn(viewModelScope)
    }
}