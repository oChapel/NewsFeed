package ua.com.foxminded.newsfeed.data

import kotlinx.coroutines.flow.Flow
import ua.com.foxminded.newsfeed.data.dao.NewsDao
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.data.dto.NewsSchema
import ua.com.foxminded.newsfeed.data.network.NewsNetwork

class DefaultNewsRepository(
    private val localDataSource: NewsDao,
    private val remoteDataSource: NewsNetwork
) : NewsRepository {

    override suspend fun loadAllNews(page: Int): List<NewsSchema> {
        return remoteDataSource.getAllNews(page, NewsRepository.MULTIPLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun getNytNews(page: Int): NewsSchema {
        return remoteDataSource.getNytNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun getCnnNews(page: Int): NewsSchema {
        return remoteDataSource.getCnnNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
    }

    override suspend fun getWiredNews(page: Int): NewsSchema {
        return remoteDataSource.getWiredNews(page, NewsRepository.SINGLE_SOURCE_PAGE_SIZE)
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
