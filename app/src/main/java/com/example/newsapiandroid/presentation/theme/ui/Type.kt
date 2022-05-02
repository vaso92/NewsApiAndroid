package com.example.newsapiandroid.presentation.theme.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.newsapiandroid.R

val brandedFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.crystasea,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
    )
)

val smallTypography = Typography()

val sw360Typography = Typography()
