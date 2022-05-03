package com.example.newsapiandroid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsapiandroid.data.db.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}