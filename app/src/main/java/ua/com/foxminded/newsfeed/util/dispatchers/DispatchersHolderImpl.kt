package ua.com.foxminded.newsfeed.util.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatchersHolderImpl : DispatchersHolder {

    override fun getMain(): CoroutineDispatcher = Dispatchers.Main

    override fun getIO(): CoroutineDispatcher = Dispatchers.IO
}
