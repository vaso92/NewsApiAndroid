package com.example.newsapiandroid.presentation.common

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.example.newsapiandroid.R
import com.example.newsapiandroid.presentation.theme.ui.Typogr
import com.example.newsapiandroid.presentation.theme.ui.brandedFontFamily

@Composable
fun BrandedAppName(
    modifier: Modifier = Modifier,
    style: TextStyle = Typogr.h3.copy(fontFamily = brandedFontFamily)
) {
    Text(
        text = stringResource(id = R.string.app_name),
        modifier = modifier,
        style = style
    )
}