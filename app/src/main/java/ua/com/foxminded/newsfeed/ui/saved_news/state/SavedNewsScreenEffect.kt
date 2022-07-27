package ua.com.foxminded.newsfeed.ui.saved_news.state

import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.mvi.states.AbstractEffect
import ua.com.foxminded.newsfeed.ui.saved_news.SavedNewsContract

sealed class SavedNewsScreenEffect : AbstractEffect<SavedNewsContract.View>() {

    class ShowErrorToast(private val stringId: Int) : SavedNewsScreenEffect() {
        override fun handle(screen: SavedNewsContract.View) {
            screen.showToast(stringId)
        }
    }

    class ShowUndoSnackBar(private val article: Item) : SavedNewsScreenEffect() {
        override fun handle(screen: SavedNewsContract.View) {
            screen.showUndoSnackBar(article)
        }
    }
}