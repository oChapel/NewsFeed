package ua.com.foxminded.newsfeed.data.dao

import android.content.Context
import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dto.Article

class RoomNewsDao(appCtx: Context) : NewsDao {

    private val articleDao: ArticleDao

    init {
        val database = ArticleDatabase.getInstance(appCtx)
        articleDao = database.articleDao()
    }

    override suspend fun insertArticle(article: Article) {
        articleDao.insertArticle(article)
    }

    override fun getAllArticlesFlow(): Flow<List<Article>> {
        return articleDao.getAllArticlesFlow()
    }

    override suspend fun existsInDb(guid: String): Boolean {
        return articleDao.existsInDb(guid)
    }

    override suspend fun deleteArticleByGuid(guid: String) {
        articleDao.deleteArticleByGuid(guid)
    }

    override suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }
}