package ua.com.foxminded.newsfeed.ui.news_list.state

import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.news_list.NewsListContract

sealed class NewsListScreenEffect : AbstractEffect<NewsListContract.View>() {

    class ShowToast(private val stringId: Int) : NewsListScreenEffect() {

        override fun handle(screen: NewsListContract.View) {
            screen.setProgress(false)
            screen.showToast(stringId)
        }
    }
}