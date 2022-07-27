package ua.com.foxminded.newsfeed.ui.saved_news

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.fragments.FragmentContract
import ua.com.foxminded.newsfeed.ui.saved_news.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.saved_news.state.SavedNewsScreenState

class SavedNewsContract {

    interface ViewModel : FragmentContract.ViewModel<SavedNewsScreenState, SavedNewsScreenEffect> {
        fun onArticleStateChanged(article: Item)
    }

    interface View : FragmentContract.View {
        fun showNews(list: List<Item>)
        fun showEmptyScreen()
        fun showToast(stringId: Int)
        fun showUndoSnackBar(article: Item)
    }

    interface Host : FragmentContract.Host
}