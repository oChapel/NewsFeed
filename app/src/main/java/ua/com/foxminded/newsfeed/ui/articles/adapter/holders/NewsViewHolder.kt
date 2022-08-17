package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ua.com.foxminded.newsfeed.data.dto.Article

abstract class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(article: Article) {
    }
}
