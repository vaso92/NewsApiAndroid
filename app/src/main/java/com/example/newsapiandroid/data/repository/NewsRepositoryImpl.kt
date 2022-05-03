package com.example.newsapiandroid.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.newsapiandroid.common.Constants
import com.example.newsapiandroid.data.paging.NewsPagingSource
import com.example.newsapiandroid.data.remote.NewsApi
import com.example.newsapiandroid.domain.repository.NewsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    @ApplicationContext private val context: Context
) : NewsRepository {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS)
    }

    override suspend fun setSearchKeywords(value: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(Constants.SEARCH_KEYWORDS)] = value
        }
    }

    override val getSearchKeywords: Flow<String?>
        get() = context.dataStore.data.map {
            it[stringPreferencesKey(Constants.SEARCH_KEYWORDS)]
        }

    override fun getNews(keywords: String, pageSize: Int) = Pager(
        pagingSourceFactory = {
            NewsPagingSource(
                newsApi = newsApi,
                keywords = keywords,
                pageSize = pageSize
            )
        },
        config = PagingConfig(
            pageSize = 20
        )
    ).flow
}