package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.models.network.NewsNetwork
import ua.com.foxminded.newsfeed.models.network.NewsTestNetwork
import javax.inject.Singleton

@Module
class NetworkTestModule {

    @Provides
    @Singleton
    fun provideTestNetwork(): NewsNetwork {
        return NewsTestNetwork()
    }
}
