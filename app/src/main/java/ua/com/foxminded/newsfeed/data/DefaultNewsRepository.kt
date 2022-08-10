package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dao.NewsDao
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsResponse
import ua.com.foxminded.newsfeed.data.network.NewsNetwork

class DefaultNewsRepository(
    private val localDataSource: NewsDao,
    private val remoteDataSource: NewsNetwork
) : NewsRepository {

    override suspend fun loadAllNews(): List<NewsResponse> {
        return remoteDataSource.getAllNews()
    }

    override suspend fun getNytNews(): NewsResponse {
        return remoteDataSource.getNytNews()
    }

    override suspend fun getCnnNews(): NewsResponse {
        return remoteDataSource.getCnnNews()
    }

    override suspend fun getWiredNews(): NewsResponse {
        return remoteDataSource.getWiredNews()
    }

    override suspend fun saveArticle(article: Article) {
        localDataSource.insertArticle(article)
    }

    override fun getAllArticlesFromDb(): Flow<List<Article>> {
        return localDataSource.getAllArticlesFlow()
    }

    override suspend fun existsInDb(guid: String): Boolean {
        return localDataSource.existsInDb(guid)
    }

    override suspend fun deleteArticleByGuid(guid: String) {
        localDataSource.deleteArticleByGuid(guid)
    }

    override suspend fun deleteArticle(article: Article) {
        localDataSource.deleteArticle(article)
    }
}
