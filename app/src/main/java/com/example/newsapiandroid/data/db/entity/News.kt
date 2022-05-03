package com.example.newsapiandroid.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsapiandroid.data.remote.dto.Article
import com.example.newsapiandroid.data.remote.dto.Source

@Entity
data class ArticleEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val sourceId: String,
    val sourceName: String,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String
) {
    companion object {
        fun from(article: Article): ArticleEntity {
            return ArticleEntity(
                sourceId = article.source.id,
                sourceName = article.source.name,
                author = article.author,
                content = article.content,
                description = article.description,
                publishedAt = article.publishedAt,
                title = article.title,
                url = article.url,
                urlToImage = article.urlToImage
            )
        }
    }

    fun toArticle(): Article {
        return Article(
            source = Source(this.sourceId, this.sourceName),
            author = this.author,
            content = this.content,
            publishedAt = this.publishedAt,
            description = this.description,
            title = this.title,
            url = this.url,
            urlToImage = this.urlToImage
        )
    }
}