package com.example.newsapiandroid.data.remote.dto

data class AllNews(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)