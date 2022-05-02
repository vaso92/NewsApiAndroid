package com.example.newsapiandroid.presentation.article_detail.view;

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.presentation.article_detail.ArticleDetailViewModel
import com.example.newsapiandroid.presentation.common.BrandedAppName
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ArticleDetail(
    article: Article,
    navigator: DestinationsNavigator,
    newsListViewModel: ArticleDetailViewModel = hiltViewModel()
) {
    ArticleDetailInternal(
        article = article,
        onBackPressed = { navigator.popBackStack() },
        onFavoritePressed = {}
    )
}

@Composable
fun ArticleDetailInternal(
    article: Article,
    onBackPressed: () -> Unit,
    onFavoritePressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    BrandedAppName()
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, "back")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    IconButton(onClick = onFavoritePressed) {
                        Icon(Icons.Outlined.Bookmark, "favorite")
                    }
                },
                elevation = Dimens.grid_2,
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Dimens.grid_4, vertical = Dimens.grid_2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = article.title, textAlign = TextAlign.Center, style = Typogr.h5)
            Text(text = article.content, style = Typogr.body1)
        }
    }
}
