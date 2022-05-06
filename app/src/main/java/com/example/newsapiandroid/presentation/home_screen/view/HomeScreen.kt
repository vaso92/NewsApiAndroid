package com.example.newsapiandroid.presentation.home_screen.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.newsapiandroid.presentation.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomeScreen() {
    val navController: NavHostController = rememberNavController()

    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController
    ) {

    }
}
