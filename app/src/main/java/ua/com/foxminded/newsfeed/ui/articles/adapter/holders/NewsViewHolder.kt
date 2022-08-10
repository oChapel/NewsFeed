package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.com.foxminded.newsfeed.data.dto.Article

open class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(article: Article) {
    }

    open fun setUpListeners() {
    }

    open fun clearListeners() {
    }

    companion object {
        const val EMPTY_STRING = ""
    }
}
