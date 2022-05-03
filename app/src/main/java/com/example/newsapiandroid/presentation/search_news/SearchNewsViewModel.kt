package com.example.newsapiandroid.presentation.search_news

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.newsapiandroid.domain.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) :
    ViewModel() {
    val searchText = mutableStateOf("")

    fun onSearchPressed() = runBlocking {
        newsRepository.setSearchKeywords(searchText.value)
    }
}