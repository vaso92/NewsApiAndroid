package com.example.newsapiandroid.presentation.news_list.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapiandroid.presentation.news_list.NewsListState
import com.example.newsapiandroid.presentation.news_list.NewsListViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalFoundationApi
@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun NewsList(
    navigator: DestinationsNavigator,
    newsListViewModel: NewsListViewModel = hiltViewModel()
) {
    when (val state = newsListViewModel.state.value) {
        NewsListState.Loading -> {
            CircularProgressIndicator()
        }
        is NewsListState.Error -> {
            Text(state.error)
        }
        is NewsListState.Success -> {
            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = 320.dp),
                state = rememberLazyListState(),
                contentPadding = PaddingValues(Dimens.grid_1_5)
            ) {
                items(state.news.count()) { index ->
                    Card(
                        modifier = Modifier
                            .padding(Dimens.grid_1)
                            .fillMaxSize()
                    ) {
                        val article = state.news[index]
                        Column {
                            Text(
                                text = article.title,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            Text(text = article.description)
                        }
                    }
                }
            }
        }
    }
}