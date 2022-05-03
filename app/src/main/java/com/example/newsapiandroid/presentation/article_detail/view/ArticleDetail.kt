package com.example.newsapiandroid.presentation.article_detail.view;

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapiandroid.R
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.presentation.article_detail.ArticleDetailViewModel
import com.example.newsapiandroid.presentation.common.BrandedAppName
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.LinkBlue
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Destination
@Composable
fun ArticleDetail(
    article: Article,
    navigator: DestinationsNavigator,
    newsListViewModel: ArticleDetailViewModel = hiltViewModel()
) {
    ArticleDetailInternal(
        article = article,
        isBookmarked = newsListViewModel.isBookmarked.collectAsState().value,
        onBackPressed = { navigator.popBackStack() },
        onBookmarkPressed = {
            newsListViewModel.onBookmarkPressed()
        }
    )
}

@Composable
fun ArticleDetailInternal(
    article: Article,
    isBookmarked: Boolean,
    onBackPressed: () -> Unit,
    onBookmarkPressed: () -> Unit
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
                    IconButton(onClick = onBookmarkPressed) {
                        Icon(
                            if (isBookmarked) {
                                Icons.Filled.Bookmark
                            } else {
                                Icons.Outlined.BookmarkAdd
                            }, "bookmark"
                        )
                    }
                },
                elevation = Dimens.grid_2,
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(Dimens.grid_2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.size(Dimens.grid_2))
                Text(text = article.title, textAlign = TextAlign.Center, style = Typogr.h5)
                Spacer(modifier = Modifier.size(Dimens.grid_2))
            }
            item {
                GlideImage(
                    imageModel = article.urlToImage,
                    contentScale = ContentScale.Crop,
                )
                PublishedAt(date = article.publishedAt)
                Spacer(modifier = Modifier.size(Dimens.grid_2))
            }
            item {
                Text(text = article.content, style = Typogr.body1)
                Spacer(modifier = Modifier.size(Dimens.grid_2))
                Row {
                    SourceLink(sourceName = article.source.name, sourceUrl = article.url)
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = stringResource(
                            id = R.string.article_detail_author,
                            article.author
                        ),
                        style = Typogr.caption
                    )
                }
                Spacer(modifier = Modifier.size(Dimens.grid_2))
            }
        }
    }
}

@Composable
private fun PublishedAt(date: String) {
    runCatching {
        val zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss")
        zonedDateTime.format(formatter)
    }.onSuccess {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Text(text = it, style = Typogr.overline)
        }
    }
}

@Composable
private fun SourceLink(
    sourceName: String,
    sourceUrl: String
) {
    val id = "inlineContent"
    val source = buildAnnotatedString {
        withStyle(style = SpanStyle(color = LinkBlue, textDecoration = TextDecoration.Underline)) {
            append(
                stringResource(
                    id = R.string.article_detail_source,
                    sourceName
                )
            )
            appendInlineContent(id = id, "external link")
        }
    }
    val inlineContent = mapOf(
        Pair(
            id,
            InlineTextContent(
                Placeholder(
                    width = Typogr.caption.fontSize,
                    height = Typogr.caption.fontSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Bottom
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Launch,
                    contentDescription = "external link",
                    tint = LinkBlue
                )
            }
        )
    )

    val uriHandler = LocalUriHandler.current

    BasicText(
        text = source,
        modifier = Modifier.clickable {
            uriHandler.openUri(sourceUrl)
        },
        inlineContent = inlineContent,
        style = Typogr.caption
    )
}
