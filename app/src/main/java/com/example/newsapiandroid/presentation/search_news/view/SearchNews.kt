package com.example.newsapiandroid.presentation.search_news.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapiandroid.presentation.destinations.NewsListDestination
import com.example.newsapiandroid.presentation.search_news.SearchNewsViewModel
import com.example.newsapiandroid.presentation.theme.ui.Dimens
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun SearchNews(
    navigator: DestinationsNavigator,
    searchNewsViewModel: SearchNewsViewModel = hiltViewModel()
) {
    SearchNewsInternal(
        onBackPressed = { navigator.popBackStack() },
        searchText = searchNewsViewModel.searchText.value,
        onTextChanged = { searchNewsViewModel.searchText.value = it },
        onSearchPressed = {
            searchNewsViewModel.onSearchPressed()
            navigator.navigate(NewsListDestination)
        }
    )
}

@Composable
private fun SearchNewsInternal(
    onBackPressed: () -> Unit,
    searchText: String,
    onTextChanged: (String) -> Unit,
    onSearchPressed: (String) -> Unit
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Row(modifier = Modifier.padding(top = Dimens.grid_2, end = Dimens.grid_2)) {
        IconButton(onClick = onBackPressed) {
            Icon(Icons.Filled.ArrowBack, "back")
        }
        TextField(
            value = searchText,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            textStyle = Typogr.body2,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearchPressed(searchText)
            }),
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            softwareKeyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}