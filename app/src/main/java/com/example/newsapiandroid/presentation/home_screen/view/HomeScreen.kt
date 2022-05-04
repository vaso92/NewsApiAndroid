package com.example.newsapiandroid.presentation.home_screen.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newsapiandroid.R
import com.example.newsapiandroid.presentation.NavGraphs
import com.example.newsapiandroid.presentation.common.BrandedAppName
import com.example.newsapiandroid.presentation.destinations.SavedArticlesDestination
import com.example.newsapiandroid.presentation.destinations.SearchNewsDestination
import com.example.newsapiandroid.presentation.news_list.view.TopBar
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
                onClick = { navController.navigate(SavedArticlesDestination.route) },
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