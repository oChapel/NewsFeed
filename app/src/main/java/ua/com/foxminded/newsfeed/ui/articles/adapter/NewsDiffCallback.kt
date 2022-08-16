package ua.com.foxminded.newsfeed.ui.articles.adapter

import androidx.recyclerview.widget.DiffUtil
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.NewsItem

object NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {

    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return if (oldItem is Article && newItem is Article) {
            oldItem.guid == newItem.guid
        } else {
            oldItem::class == newItem::class
        }
    }

    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
        return if (oldItem is Article && newItem is Article) {
            oldItem == newItem
        } else {
            areItemsTheSame(oldItem, newItem)
        }
    }
}
