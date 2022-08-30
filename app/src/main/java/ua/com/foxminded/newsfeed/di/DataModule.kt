package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.models.DefaultNewsRepository
import ua.com.foxminded.newsfeed.models.NewsRepository
import ua.com.foxminded.newsfeed.models.dao.NewsDao
import ua.com.foxminded.newsfeed.models.dao.RoomNewsDao
import ua.com.foxminded.newsfeed.models.network.NewsNetwork
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun provideNewsRepository(
        localDataSource: NewsDao, remoteDataSource: NewsNetwork
    ): NewsRepository {
        return DefaultNewsRepository(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(): NewsDao {
        return RoomNewsDao(App.instance)
    }
}
