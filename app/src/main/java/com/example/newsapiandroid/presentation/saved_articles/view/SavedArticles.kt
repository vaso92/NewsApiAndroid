package com.example.newsapiandroid.presentation.saved_articles.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import com.example.newsapiandroid.presentation.news_list.view.NewsListInternal
import com.example.newsapiandroid.presentation.saved_articles.SavedArticlesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SavedArticles(
    navigator: DestinationsNavigator,
    savedArticlesViewModel: SavedArticlesViewModel = hiltViewModel()
) {
    val news = savedArticlesViewModel.savedArticles().collectAsLazyPagingItems()

    NewsListInternal(
        news = news,
        onArticleSelected = { article ->
            navigator.navigate(ArticleDetailDestination(article))
        }
    )
}