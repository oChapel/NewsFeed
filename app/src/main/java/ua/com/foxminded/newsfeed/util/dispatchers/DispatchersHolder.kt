package ua.com.foxminded.newsfeed.util.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersHolder {

    fun getMain(): CoroutineDispatcher

    fun getIO(): CoroutineDispatcher
}
