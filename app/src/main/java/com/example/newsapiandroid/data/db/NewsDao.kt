package com.example.newsapiandroid.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.newsapiandroid.data.db.entity.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert(entity = ArticleEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: ArticleEntity)

    @Delete(entity = ArticleEntity::class)
    fun deleteArticle(article: ArticleEntity)

    @Query("SELECT * FROM articleentity WHERE url =:url")
    fun getArticle(url: String): Flow<ArticleEntity?>

    @Query("SELECT * FROM articleentity")
    fun getSavedArticles(): PagingSource<Int, ArticleEntity>
}