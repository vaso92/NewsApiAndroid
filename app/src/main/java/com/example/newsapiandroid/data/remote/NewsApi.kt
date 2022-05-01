package com.example.newsapiandroid.data.remote

import com.example.newsapiandroid.common.Constants
import com.example.newsapiandroid.data.remote.dto.AllNews
import retrofit2.http.GET

interface NewsApi {
    @GET(Constants.EVERYTHING_URL)
    suspend fun getNews(): AllNews
}