package com.example.newsapiandroid.presentation.news_list.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapiandroid.R
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.data.remote.dto.Source
import com.example.newsapiandroid.presentation.news_list.NewsListState
import com.example.newsapiandroid.presentation.news_list.NewsListViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.NewsApiAndroidTheme
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.example.newsapiandroid.presentation.theme.ui.brandedFontFamily
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
    val state = newsListViewModel.state.value

    NewsListInternal(state = state)
}

@Composable
private fun NewsListInternal(state: NewsListState) {
    Scaffold(topBar = {
        TopBar(
            onFilter = {},
            onSearch = {}
        )
    }) {
        when (state) {
            NewsListState.Loading -> {
                CircularProgressIndicator()
            }
            is NewsListState.Error -> {
                Text(state.error)
            }
            is NewsListState.Success -> {
                LazyVerticalGrid(
                    cells = GridCells.Adaptive(minSize = 280.dp),
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(Dimens.grid_1_5)
                ) {
                    items(state.news.count()) { index ->
                        val article = state.news[index]

                        Article(article)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    onFilter: () -> Unit,
    onSearch: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = Typogr.h3.copy(fontFamily = brandedFontFamily)
            )
        },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, "menu")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            IconButton(onClick = onFilter) {
                Icon(Icons.Filled.Tune, "filter")
            }
            IconButton(onClick = onSearch) {
                Icon(Icons.Filled.Search, "search")
            }
        },
        elevation = Dimens.grid_2,
    )
}

@Composable
private fun Article(article: Article) {
    Card(
        modifier = Modifier
            .padding(Dimens.grid_1)
            .fillMaxSize()
    ) {
        Column {
            Text(
                text = article.title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typogr.h6
            )
            Text(text = article.description, style = Typogr.body2)
        }
    }
}

@Preview
@Composable
private fun NewsListPreview() {
    NewsApiAndroidTheme {
        NewsListInternal(
            state = NewsListState.Success(
                news = listOf(
                    Article(
                        source = Source(
                            "",
                            name = ""
                        ),
                        author = "",
                        title = "Nintendo Switch Sports hands-on: Reviving a surefire formula for fun",
                        description = "It’s hard to believe Wii Sports came out more than 15 years ago. But to me, the strangest thing is that despite being one of the most memorable Wii games of all time, Nintendo never made a proper sequel, that is until now.I got a chance to check out Nintendo …",
                        content = "",
                        publishedAt = "",
                        url = "",
                        urlToImage = ""
                    )
                )
            )
        )
    }
}
