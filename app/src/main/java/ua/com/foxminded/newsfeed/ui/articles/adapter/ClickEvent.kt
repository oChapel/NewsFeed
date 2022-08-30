package ua.com.foxminded.newsfeed.ui.articles.adapter

import ua.com.foxminded.newsfeed.models.dto.Article

sealed class ClickEvent {

    class OnItemClicked(val article: Article) : ClickEvent()

    class OnBookmarkClicked(val article: Article) : ClickEvent()
}
