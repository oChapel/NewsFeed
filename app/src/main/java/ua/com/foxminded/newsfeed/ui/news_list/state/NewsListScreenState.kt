package ua.com.foxminded.newsfeed.ui.news_list.state

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.states.AbstractState
import ua.com.foxminded.newsfeed.ui.news_list.NewsListContract

open class NewsListScreenState protected constructor(
    val isProgressVisible: Boolean = false,
    val list: List<Item> = listOf()
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

    class LoadNews(list: List<Item>) : NewsListScreenState(false, list) {
        override fun merge(prevState: NewsListScreenState): NewsListScreenState {
            return NewsListScreenState(isProgressVisible, list)
        }
    }
}