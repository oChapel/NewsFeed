package ua.com.foxminded.newsfeed.ui.news_list

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenEffect
import ua.com.foxminded.newsfeed.ui.news_list.state.NewsListScreenState

class NewsListContract {

    interface ViewModel : FragmentContract.ViewModel<NewsListScreenState, NewsListScreenEffect> {
        fun reload()
    }

    interface View : FragmentContract.View {
        fun setProgressVisibility(isVisible: Boolean)
        fun showNews(list: List<Item>)
        fun showToast(stringId: Int)
    }

    interface Host : FragmentContract.Host
}