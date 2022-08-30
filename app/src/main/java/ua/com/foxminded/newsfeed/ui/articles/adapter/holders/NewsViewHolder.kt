package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.com.foxminded.newsfeed.models.dto.NewsItem

abstract class NewsViewHolder<T : NewsItem>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(article: T) {
    }
}
