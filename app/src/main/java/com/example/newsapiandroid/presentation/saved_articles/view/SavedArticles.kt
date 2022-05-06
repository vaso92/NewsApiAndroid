package com.example.newsapiandroid.presentation.saved_articles.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapiandroid.presentation.common.SideDrawer
import com.example.newsapiandroid.presentation.common.SideMenuItem
import com.example.newsapiandroid.presentation.common.TopBar
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import com.example.newsapiandroid.presentation.news_list.view.NewsListContent
import com.example.newsapiandroid.presentation.saved_articles.SavedArticlesViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SavedArticles(
    navigator: DestinationsNavigator,
    savedArticlesViewModel: SavedArticlesViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val news = savedArticlesViewModel.savedArticles().collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, "menu")
                    }
                },
            )
        },
        drawerContent = {
            SideDrawer(
                destinationsNavigator = navigator,
                selectedItem = SideMenuItem.SavedArticles,
                scaffoldState = scaffoldState
            )
        }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            NewsListContent(
                news = news,
                onArticleSelected = { article ->
                    navigator.navigate(ArticleDetailDestination(article))
                }
            )
        }
    }
}