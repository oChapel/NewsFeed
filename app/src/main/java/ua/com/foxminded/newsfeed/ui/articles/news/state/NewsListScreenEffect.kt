package ua.com.foxminded.newsfeed.ui.articles.news.state

import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract

sealed class NewsListScreenEffect : AbstractEffect<NewsListContract.View>() {

    class ShowError(private val error: Throwable) : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.setProgress(false)
            screen.showErrorDialog(error)
        }
    }
}
