package ua.com.foxminded.newsfeed.ui.articles.news.state

import ua.com.foxminded.newsfeed.data.NewsItem
import ua.com.foxminded.newsfeed.mvi.states.AbstractState
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract

open class NewsListScreenState protected constructor(
    val isProgressVisible: Boolean = false,
    val list: List<NewsItem> = listOf()
) : AbstractState<NewsListContract.View, NewsListScreenState>() {

    override fun visit(screen: NewsListContract.View) {
        screen.setProgress(isProgressVisible)
        screen.showNews(list)
    }

    class Loading : NewsListScreenState(true) {
        override fun merge(prevState: NewsListScreenState): NewsListScreenState {
            return NewsListScreenState(isProgressVisible, prevState.list)
        }
    }

    class LoadNews(list: List<NewsItem>) : NewsListScreenState(false, list) {
        override fun merge(prevState: NewsListScreenState): NewsListScreenState {
            return NewsListScreenState(isProgressVisible, list)
        }
    }

    class Error : NewsListScreenState(false)
}
