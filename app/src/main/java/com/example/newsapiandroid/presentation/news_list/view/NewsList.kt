package com.example.newsapiandroid.presentation.news_list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapiandroid.R
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.data.remote.dto.Source
import com.example.newsapiandroid.presentation.common.BrandedAppName
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import com.example.newsapiandroid.presentation.news_list.NewsListViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.NewsApiAndroidTheme
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOf

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun NewsList(
    navigator: DestinationsNavigator,
    newsListViewModel: NewsListViewModel = hiltViewModel()
) {
    val searchKeywords = newsListViewModel.searchKeywords.collectAsState(initial = null).value
    val news =
        searchKeywords?.let { newsListViewModel.news(searchKeywords).collectAsLazyPagingItems() }

    NewsListInternal(
        news = news,
        onArticleSelected = { article ->
            navigator.navigate(ArticleDetailDestination(article))
        },
    )
}

@Composable
fun NewsListInternal(
    news: LazyPagingItems<Article>?,
    onArticleSelected: (Article) -> Unit,
) {
    if (news != null) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            state = rememberLazyGridState()
        ) {
            items(news.itemCount) { index ->
                val article = news[index]
                article?.let {
                    Article(article = it, onArticleSelected = onArticleSelected)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            news.apply {
                when (loadState.refresh) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is LoadState.Error -> {
                        if (news.itemCount == 0) {
                            Text(
                                text = stringResource(id = R.string.news_list_saved_failed_to_load),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = Typogr.body1
                            )
                        }
                    }
                    is LoadState.NotLoading -> {
                        if (news.itemCount == 0) {
                            Text(
                                text = stringResource(id = R.string.news_list_saved_no_results_found),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = Typogr.body1
                            )
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun TopBar(
    onMenuPressed: () -> Unit,
    onFilterPressed: () -> Unit,
    onSearchPressed: () -> Unit,
) {
    TopAppBar(
        title = {
            BrandedAppName()
        },
        navigationIcon = {
            IconButton(onClick = onMenuPressed) {
                Icon(Icons.Filled.Menu, "menu")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            IconButton(onClick = onFilterPressed) {
                Icon(Icons.Filled.Tune, "filter")
            }
            IconButton(onClick = onSearchPressed) {
                Icon(Icons.Filled.Search, "search")
            }
        },
        elevation = Dimens.grid_2,
    )
}

@Composable
private fun Article(
    article: Article,
    onArticleSelected: (Article) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(Dimens.grid_1)
            .fillMaxSize()
            .clickable {
                onArticleSelected(article)
            }
    ) {
        Column {
            Text(
                text = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.grid_2),
                textAlign = TextAlign.Center,
                style = Typogr.h6
            )
            Text(
                text = article.description,
                modifier = Modifier.padding(Dimens.grid_2),
                style = Typogr.body2
            )
        }
    }
}

@Preview
@Composable
private fun NewsListPreview() {
    NewsApiAndroidTheme {
        NewsListInternal(
            news = flowOf(
                PagingData.from(
                    MutableList(100) {
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
                    }
                )
            ).collectAsLazyPagingItems(),
            onArticleSelected = {},
        )
    }
}
