package ua.com.foxminded.newsfeed.ui.articles.saved.state

import ua.com.foxminded.newsfeed.data.dto.Item
import ua.com.foxminded.newsfeed.mvi.states.AbstractState
import ua.com.foxminded.newsfeed.ui.articles.saved.SavedNewsContract

sealed class SavedNewsScreenState : AbstractState<SavedNewsContract.View, SavedNewsScreenState>() {

    class ShowNews(private val list: List<Item>) : SavedNewsScreenState() {
        override fun visit(screen: SavedNewsContract.View) {
            screen.showNews(list)
        }
    }

    object ShowEmptyScreen : SavedNewsScreenState() {
        override fun visit(screen: SavedNewsContract.View) {
            screen.showEmptyScreen()
        }
    }
}
