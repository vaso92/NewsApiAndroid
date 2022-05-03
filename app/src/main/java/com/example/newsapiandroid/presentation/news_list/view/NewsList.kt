package com.example.newsapiandroid.presentation.news_list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.newsapiandroid.presentation.destinations.SavedArticlesDestination
import com.example.newsapiandroid.presentation.destinations.SearchNewsDestination
import com.example.newsapiandroid.presentation.news_list.NewsListViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.NewsApiAndroidTheme
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun NewsList(
    navigator: DestinationsNavigator,
    newsListViewModel: NewsListViewModel = hiltViewModel()
) {
    val searchKeywords = newsListViewModel.searchKeywords.collectAsState(initial = null).value
    val news = newsListViewModel.news(searchKeywords).collectAsLazyPagingItems()
    NewsListInternal(
        news = news,
        onArticleSelected = { article ->
            navigator.navigate(ArticleDetailDestination(article))
        },
        onSearchPressed = {
            navigator.navigate(SearchNewsDestination)
        },
        onSavedArticlesPressed = {
            navigator.navigate(SavedArticlesDestination)
        }
    )
}

@Composable
private fun NewsListInternal(
    news: LazyPagingItems<Article>,
    onArticleSelected: (Article) -> Unit,
    onSearchPressed: () -> Unit,
    onSavedArticlesPressed: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                onMenuPressed = {
                    if (scaffoldState.drawerState.isClosed) {
                        scope.launch { scaffoldState.drawerState.open() }
                    }
                },
                onFilterPressed = {},
                onSearchPressed = onSearchPressed,
            )
        },
        drawerContent = {
            Row {
                TopAppBar(
                    title = {
                        BrandedAppName()
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (scaffoldState.drawerState.isOpen) {
                                scope.launch { scaffoldState.drawerState.close() }
                            }
                        }) {
                            Icon(Icons.Filled.Close, "close drawer")
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.size(Dimens.grid_2))
            Button(
                onClick = onSavedArticlesPressed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.grid_2)
            ) {
                Row(Modifier.fillMaxWidth(1f)) {
                    Icon(
                        imageVector = Icons.Outlined.Bookmark,
                        contentDescription = "favorite"
                    )
                    Spacer(modifier = Modifier.width(Dimens.grid_5))
                    Text(
                        text = stringResource(id = R.string.news_list_saved_articles_button),
                        style = Typogr.button
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(Dimens.grid_2)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        },
    ) { contentPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 280.dp),
            state = rememberLazyGridState(),
            contentPadding = contentPadding
        ) {
            items(news.itemCount) { index ->
                val article = news[index]
                article?.let {
                    Article(article = it, onArticleSelected = onArticleSelected)
                }
            }
        }

        news.apply {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is LoadState.Error -> {

                }
                else -> {}
            }
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
            onSearchPressed = {},
            onSavedArticlesPressed = {}
        )
    }
}
