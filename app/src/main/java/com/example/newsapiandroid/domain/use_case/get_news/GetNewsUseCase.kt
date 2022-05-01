package com.example.newsapiandroid.domain.use_case.get_news

import com.example.newsapiandroid.common.Resource
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    operator fun invoke(): Flow<Resource<List<Article>>> = flow {
        try {
            emit(Resource.Loading())
            val news = newsRepository.getNews()
            emit(Resource.Success(news.articles))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown HttpException"))
        } catch (e: IOException) {
            emit(Resource.Error("Unable to contact server, check your internet connection."))
        }
    }
}