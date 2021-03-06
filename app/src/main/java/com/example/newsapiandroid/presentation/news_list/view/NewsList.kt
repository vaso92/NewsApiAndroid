package com.example.newsapiandroid.presentation.news_list.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.runtime.*
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
import com.example.newsapiandroid.data.paging.SortBy
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.data.remote.dto.Source
import com.example.newsapiandroid.presentation.article_detail.view.ArticleDetailContent
import com.example.newsapiandroid.presentation.article_detail.view.ArticleDetailInternal
import com.example.newsapiandroid.presentation.article_detail.view.PublishedAt
import com.example.newsapiandroid.presentation.common.SideDrawer
import com.example.newsapiandroid.presentation.common.SideMenuItem
import com.example.newsapiandroid.presentation.common.TopBar
import com.example.newsapiandroid.presentation.destinations.SearchNewsDestination
import com.example.newsapiandroid.presentation.news_list.NewsListViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.NewsApiAndroidTheme
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.getstream.butterfly.compose.LocalWindowDpSize
import io.getstream.butterfly.compose.WindowDpSize
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import timber.log.Timber

@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
@Composable
fun NewsList(
    navigator: DestinationsNavigator,
    newsListViewModel: NewsListViewModel = hiltViewModel()
) {
    val searchKeywords = newsListViewModel.searchKeywords.collectAsState(initial = null).value
    val sortBy = newsListViewModel.sortBy.collectAsState(initial = null).value
    val news =
        searchKeywords?.let {
            sortBy?.let {
                newsListViewModel.news(searchKeywords, sortBy).collectAsLazyPagingItems()
            }
        }

    val selectedArticle = newsListViewModel.selectedArticle.collectAsState().value
    val isBookmarked = newsListViewModel.isBookmarked.collectAsState().value

    if (selectedArticle != null &&
        (LocalWindowDpSize.current is WindowDpSize.Compact ||
                LocalWindowDpSize.current is WindowDpSize.Medium)
    ) {
        ArticleDetailInternal(
            article = selectedArticle,
            isBookmarked = isBookmarked,
            onBackPressed = { newsListViewModel.onArticleSelected(null) },
            onBookmarkPressed = newsListViewModel::onBookmarkPressed)
    } else {
        NewsListInternal(
            navigator = navigator,
            news = news,
            currentSortBy = sortBy,
            onFilterPressed = { newsListViewModel.setSortBy(it) },
            onSearchPressed = { navigator.navigate(SearchNewsDestination.route) },
            onArticleSelected = { article ->
                newsListViewModel.onArticleSelected(article)
            },
            selectedArticle = selectedArticle,
            onBookmarkPressed = newsListViewModel::onBookmarkPressed,
            isBookmarked = isBookmarked
        )
    }
}

@Composable
private fun NewsListInternal(
    navigator: DestinationsNavigator,
    news: LazyPagingItems<Article>?,
    currentSortBy: SortBy?,
    onSearchPressed: () -> Unit,
    onFilterPressed: (sortBy: SortBy) -> Unit,
    onArticleSelected: (Article) -> Unit,
    selectedArticle: Article?,
    onBookmarkPressed: () -> Unit,
    isBookmarked: Boolean
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, "menu")
                    }
                },
                actions = {
                    TuneDropdownMenu(
                        currentSortBy = currentSortBy,
                        onFilterPressed = onFilterPressed
                    )
                    IconButton(onClick = onSearchPressed) {
                        Icon(Icons.Filled.Search, "search")
                    }
                    if (selectedArticle != null) {
                        IconButton(onClick = onBookmarkPressed) {
                            Icon(
                                if (isBookmarked) {
                                    Icons.Filled.Bookmark
                                } else {
                                    Icons.Outlined.BookmarkAdd
                                }, "bookmark"
                            )
                        }
                    }
                },
            )
        },
        drawerContent = {
            SideDrawer(
                destinationsNavigator = navigator,
                selectedItem = SideMenuItem.NewsFeed,
                scaffoldState = scaffoldState
            )
        }
    ) { contentPadding ->
        NewsListContent(
            modifier = Modifier.padding(contentPadding),
            news = news,
            onArticleSelected = onArticleSelected,
            selectedArticle = selectedArticle
        )
    }
}

@Composable
fun NewsListContent(
    modifier: Modifier = Modifier,
    news: LazyPagingItems<Article>?,
    onArticleSelected: (Article) -> Unit,
    selectedArticle: Article?
) {
    if (news != null) {
        when (LocalWindowDpSize.current) {
            is WindowDpSize.Compact,
            is WindowDpSize.Medium -> {
                Timber.i("WindowDpSize.Compact")

                LazyColumn {
                    items(news.itemCount) { index ->
                        val article = news[index]
                        article?.let {
                            Article(article = it, onArticleSelected = onArticleSelected)
                        }
                    }
                }
            }
            is WindowDpSize.Expanded -> {
                Timber.i("WindowDpSize.Expanded")

                Row {
                    LazyColumn(Modifier.width(300.dp)) {
                        items(news.itemCount) { index ->
                            val article = news[index]
                            article?.let {
                                Article(article = it, onArticleSelected = onArticleSelected)
                            }
                        }
                    }
                    if (selectedArticle != null) {
                        ArticleDetailContent(
                            modifier = Modifier.fillMaxSize(),
                            article = selectedArticle
                        )
                    }
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
            PublishedAt(date = article.publishedAt)
        }
    }
}

@Composable
fun TuneDropdownMenu(
    currentSortBy: SortBy?,
    onFilterPressed: (SortBy) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    IconButton(onClick = {
        expanded.value = !expanded.value
    }) {
        Icon(Icons.Filled.Sort, "sort")
    }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
    ) {
        SortBy.values().forEach {
            DropdownMenuItem(onClick = { onFilterPressed(it) }) {
                Text(
                    text = it.name, color = if (currentSortBy == it) {
                        MaterialTheme.colors.primary
                    } else {
                        LocalContentColor.current
                    }, style = Typogr.button
                )
            }

            Divider()
        }
    }
}

@Preview
@Composable
private fun NewsListPreview() {
    NewsApiAndroidTheme {
        NewsListInternal(
            navigator = EmptyDestinationsNavigator,
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
                            description = "It???s hard to believe Wii Sports came out more than 15 years ago. But to me, the strangest thing is that despite being one of the most memorable Wii games of all time, Nintendo never made a proper sequel, that is until now.I got a chance to check out Nintendo ???",
                            content = "",
                            publishedAt = "",
                            url = "",
                            urlToImage = ""
                        )
                    }
                )
            ).collectAsLazyPagingItems(),
            onArticleSelected = {},
            selectedArticle = null,
            currentSortBy = SortBy.popularity,
            onFilterPressed = {},
            onSearchPressed = {},
            onBookmarkPressed = {},
            isBookmarked = false
        )
    }
}
