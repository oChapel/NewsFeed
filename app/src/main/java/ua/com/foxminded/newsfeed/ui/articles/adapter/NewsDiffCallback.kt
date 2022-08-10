package ua.com.foxminded.newsfeed.ui.articles.adapter

import androidx.recyclerview.widget.DiffUtil
import ua.com.foxminded.newsfeed.data.Article

object NewsDiffCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldArticle: Article, newArticle: Article): Boolean {
        return oldArticle.guid == newArticle.guid
    }

    override fun areContentsTheSame(oldArticle: Article, newArticle: Article): Boolean {
        return oldArticle == newArticle && oldArticle.isSaved == newArticle.isSaved
    }
}
