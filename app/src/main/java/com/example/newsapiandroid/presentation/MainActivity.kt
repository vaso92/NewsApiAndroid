package com.example.newsapiandroid.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.example.newsapiandroid.presentation.home_screen.view.HomeScreen
import com.example.newsapiandroid.presentation.theme.ui.NewsApiAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.butterfly.compose.LocalWindowDpSize
import io.getstream.butterfly.compose.rememberWindowDpSize
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        plant(DebugTree())

        setContent {
            CompositionLocalProvider(LocalWindowDpSize provides rememberWindowDpSize()) {
                NewsApiAndroidTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}
