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

    override suspend fun insertNews(list: List<Article>) {
        articleDao.insertNews(list)
    }

    override suspend fun getAllCachedNews(page: Int, pageSize: Int): List<Article> {
        return articleDao.getAllCachedNews((page + 1) * pageSize)
    }

    override suspend fun getCachedNewsBySource(
        page: Int, pageSize: Int, domain: String
    ): List<Article> {
        return articleDao.getCachedNewsBySource((page + 1) * pageSize, domain)
    }

    override fun getSavedNewsFlow(): Flow<List<Article>> {
        return articleDao.getSavedNewsFlow()
    }

    override suspend fun isBookmarked(guid: String): Boolean {
        return articleDao.isBookmarked(guid)
    }

    override suspend fun deleteArticle(article: Article) {
        articleDao.deleteArticle(article)
    }
}