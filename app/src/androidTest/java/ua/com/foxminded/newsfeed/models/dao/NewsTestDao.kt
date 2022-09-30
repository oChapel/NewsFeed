package ua.com.foxminded.newsfeed.models.dao

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.com.foxminded.newsfeed.models.dto.Article

class NewsTestDao : NewsDao {

    private val db = ArrayList<Article>()
    private val articleFlow = MutableStateFlow<List<Article>>(listOf())

    override suspend fun insertArticle(article: Article) {
        db.removeIf { it.guid == article.guid }
        db.add(article)
        articleFlow.tryEmit(getSavedNews())
    }

    override suspend fun insertNews(list: List<Article>) {
        db.addAll(list)
    }

    override suspend fun getAllCachedNews(page: Int, pageSize: Int): List<Article> {
        delay(1000L)
        return if ((page + 1) * pageSize > db.size) {
            db.sortedByDescending { it.pubDate }
        } else {
            db.subList(0, (page + 1) * pageSize - 1).sortedByDescending { it.pubDate }
        }
    }

    override suspend fun getCachedNewsBySource(
        page: Int,
        pageSize: Int,
        domain: String
    ): List<Article> {
        val list = ArrayList<Article>()
        for (article in db) {
            if (article.link.contains(domain)) list.add(article)
            if (list.size == (page + 1) * pageSize) break
        }
        return list.sortedByDescending { it.pubDate }
    }

    override fun getSavedNewsFlow(): Flow<List<Article>> = articleFlow

    override suspend fun isBookmarked(guid: String): Boolean {
        for (article in db) {
            if (article.guid == guid) return article.isBookmarked
        }
        return false
    }

    override suspend fun deleteArticle(article: Article) {
        db.remove(article)
        articleFlow.tryEmit(getSavedNews())
    }

    private fun getSavedNews(): List<Article> {
        val list = ArrayList<Article>()
        for (article in db) {
            if (article.isBookmarked) {
                list.add(article)
            }
        }
        return list.sortedByDescending { it.pubDate }
    }
}
