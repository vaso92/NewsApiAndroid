package com.example.newsapiandroid.domain.repository

import com.example.newsapiandroid.data.remote.dto.AllNews

interface NewsRepository {
    suspend fun getNews(): AllNews
}