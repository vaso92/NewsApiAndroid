package com.example.newsapiandroid.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val source: Source,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
)