package com.example.newsapiandroid.data.remote

import com.example.newsapiandroid.common.Constants
import com.example.newsapiandroid.data.remote.dto.AllNews
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET(Constants.EVERYTHING_URL)
    suspend fun getNews(
        @Query("q") keywords: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): AllNews
}