package com.example.newsapiandroid.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newsapiandroid.data.remote.NewsApi
import com.example.newsapiandroid.data.remote.dto.Article

class NewsPagingSource constructor(
    private val newsApi: NewsApi,
    private val keywords: String,
    private val pageSize: Int
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1

            val news = newsApi.getNews(keywords = keywords, pageSize = pageSize, page = page)
            val nextPage = (page + 1).takeIf { it <= news.totalResults }

            LoadResult.Page(
                data = news.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = state.anchorPosition
}
