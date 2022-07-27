package ua.com.foxminded.newsfeed.ui.news_list.state

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.news_list.NewsListContract

sealed class NewsListScreenEffect : AbstractEffect<NewsListContract.View>() {

    class ShowToast(private val stringId: Int) : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.setProgress(false)
            screen.showToast(stringId)
        }
    }

    class ItemChanged(
        private val article: Item,
        private val isArticleInDb: Boolean
    ) : NewsListScreenEffect() {
        override fun handle(screen: NewsListContract.View) {
            screen.onItemChanged(article, isArticleInDb)
        }
    }
}