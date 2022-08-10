package ua.com.foxminded.newsfeed.di

import dagger.Module
import dagger.Provides
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolder
import ua.com.foxminded.newsfeed.util.dispatchers.DispatchersHolderImpl

@Module
class AppModule {

    @Provides
    fun provideDispatchersHolder() : DispatchersHolder {
        return DispatchersHolderImpl()
    }
}
