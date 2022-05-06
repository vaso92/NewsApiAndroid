package com.example.newsapiandroid.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.newsapiandroid.R
import com.example.newsapiandroid.presentation.destinations.NewsListDestination
import com.example.newsapiandroid.presentation.destinations.SavedArticlesDestination
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SideDrawer(
    destinationsNavigator: DestinationsNavigator,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    Row {
        TopAppBar(
            title = {
                BrandedAppName()
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { scaffoldState.drawerState.close() }
                }) {
                    Icon(Icons.Filled.Close, "close drawer")
                }
            }
        )
    }

    Spacer(modifier = Modifier.size(Dimens.grid_2))

    SideMenuButton(
        onClick = {
            destinationsNavigator.navigate(SavedArticlesDestination.route)
            scope.launch { scaffoldState.drawerState.close() }
        },
        icon = Icons.Outlined.Bookmark,
        contentDescription = "favorite",
        text = stringResource(id = R.string.news_list_saved_articles_button)
    )

    SideMenuButton(
        onClick = {
            destinationsNavigator.navigate(NewsListDestination.route)
            scope.launch { scaffoldState.drawerState.close() }
        },
        icon = Icons.Outlined.Newspaper,
        contentDescription = "news feed",
        text = stringResource(id = R.string.news_list_news_feed_button)
    )
    Spacer(modifier = Modifier.size(Dimens.grid_2))
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = { BrandedAppName() },
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = Dimens.grid_2
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = contentColor,
        actions = actions,
        elevation = elevation,
    )
}

@Composable
fun SideMenuButton(
    onClick: () -> Unit,
    text: String,
    icon: ImageVector,
    contentDescription: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.grid_2)
    ) {
        Row(Modifier.fillMaxWidth(1f)) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.width(Dimens.grid_5))
            Text(
                text = text,
                style = Typogr.button
            )
        }
    }
}
