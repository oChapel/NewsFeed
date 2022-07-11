package ua.com.foxminded.newsfeed.data.dao

import android.app.Application
import ua.com.foxminded.newsfeed.data.model.Item

class RoomNewsDao(application: Application) : NewsDao {

    private val articleDao: ArticleDao

    init {
        val database = ArticleDatabase.getInstance(application)
        articleDao = database.articleDao()
    }

    override suspend fun insertArticle(article: Item) {
        articleDao.insertArticle(article)
    }

    override suspend fun getAllArticles(): List<Item> {
        return articleDao.getAllArticles()
    }

    override suspend fun deleteArticle(article: Item) {
        articleDao.deleteArticle(article)
    }
}