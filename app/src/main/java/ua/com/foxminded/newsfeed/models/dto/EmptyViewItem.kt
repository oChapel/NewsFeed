package ua.com.foxminded.newsfeed.models.dto

class EmptyViewItem : NewsItem {
    override fun getViewHolderType(): Int = NewsItem.EMPTY_VIEW
}
