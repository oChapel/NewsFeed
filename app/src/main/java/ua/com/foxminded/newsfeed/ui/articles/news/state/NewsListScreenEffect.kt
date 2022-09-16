package ua.com.foxminded.newsfeed.ui.articles.news.state

import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract

sealed class NewsListScreenEffect : AbstractEffect<NewsListContract.View>() {

    class ShowError(private val error: Throwable) : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.showErrorDialog(error)
        }
    }

    class ShowToast(private val resId: Int) : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.showToast(resId)
        }
    }

    class ShowPopupWindow : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.showPopupWindow()
        }
    }
}
