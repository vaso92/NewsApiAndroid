package com.example.newsapiandroid.presentation.home_screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapiandroid.R
import com.example.newsapiandroid.presentation.NavGraphs
import com.example.newsapiandroid.presentation.common.BrandedAppName
import com.example.newsapiandroid.presentation.destinations.ArticleDetailDestination
import com.example.newsapiandroid.presentation.destinations.NewsListDestination
import com.example.newsapiandroid.presentation.destinations.SavedArticlesDestination
import com.example.newsapiandroid.presentation.destinations.SearchNewsDestination
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@Destination
@Composable
fun HomeScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController: NavHostController = rememberNavController()
    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()

    when (currentBackStackEntryAsState?.destination?.route) {
        ArticleDetailDestination.route -> {
            Box {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    navController = navController
                ) {

                }
            }
        }
        else ->
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopBar(
                        navBackStackEntry = currentBackStackEntryAsState,
                        onMenuPressed = {
                            scope.launch { scaffoldState.drawerState.open() }
                        },
                        onFilterPressed = {},
                        onSearchPressed = { navController.navigate(SearchNewsDestination.route) },
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
                            navController.navigate(SavedArticlesDestination.route)
                            scope.launch { scaffoldState.drawerState.close() }
                        },
                        icon = Icons.Outlined.Bookmark,
                        contentDescription = "favorite",
                        text = stringResource(id = R.string.news_list_saved_articles_button)
                    )

                    SideMenuButton(
                        onClick = {
                            navController.navigate(NewsListDestination.route)
                            scope.launch { scaffoldState.drawerState.close() }
                        },
                        icon = Icons.Outlined.Newspaper,
                        contentDescription = "news feed",
                        text = stringResource(id = R.string.news_list_news_feed_button)
                    )
                    Spacer(modifier = Modifier.size(Dimens.grid_2))
                }
            ) { contentPadding ->
                Box(Modifier.padding(contentPadding)) {
                    DestinationsNavHost(
                        navGraph = NavGraphs.root,
                        navController = navController
                    ) {

                    }
                }
            }
    }

}

@Composable
private fun TopBar(
    navBackStackEntry: NavBackStackEntry?,
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
            when (navBackStackEntry?.destination?.route) {
                SearchNewsDestination.route -> {}
                NewsListDestination.route -> {
                    IconButton(onClick = onFilterPressed) {
                        Icon(Icons.Filled.Tune, "filter")
                    }
                    IconButton(onClick = onSearchPressed) {
                        Icon(Icons.Filled.Search, "search")
                    }
                }
                else -> {
                    IconButton(onClick = onSearchPressed) {
                        Icon(Icons.Filled.Search, "search")
                    }
                }
            }
        },
        elevation = Dimens.grid_2,
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
