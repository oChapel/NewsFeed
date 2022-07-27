package ua.com.foxminded.newsfeed.data.dao

import android.app.Application
import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Item

class RoomNewsDao(application: Application) : NewsDao {

    private val articleDao: ArticleDao

    init {
        val database = ArticleDatabase.getInstance(application)
        articleDao = database.articleDao()
    }

    override suspend fun insertArticle(article: Item) {
        articleDao.insertArticle(article)
    }

    override fun getAllArticles(): Flow<List<Item>> {
        return articleDao.getAllArticles()
    }

    override suspend fun existsInDb(title: String): Boolean {
        return articleDao.existsInDb(title)
    }

    override suspend fun deleteArticleByTitle(title: String) {
        articleDao.deleteArticleByTitle(title)
    }

    override suspend fun deleteArticle(article: Item) {
        articleDao.deleteArticle(article)
    }
}