package com.example.newsapiandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newsapiandroid.data.db.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(entity = ArticleEntity::class)
    fun insertArticle(article: ArticleEntity)

    @Query("SELECT * FROM articleentity")
    fun getSavedArticles(): Flow<List<ArticleEntity>>
}