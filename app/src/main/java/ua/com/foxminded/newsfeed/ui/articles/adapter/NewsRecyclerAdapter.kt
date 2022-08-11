package ua.com.foxminded.newsfeed.ui.articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.databinding.ItemCnnNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemEmptyBinding
import ua.com.foxminded.newsfeed.databinding.ItemNytNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemWiredNewsBinding
import ua.com.foxminded.newsfeed.ui.articles.adapter.ViewHolderTypeProvider.Companion.CNN_ARTICLE
import ua.com.foxminded.newsfeed.ui.articles.adapter.ViewHolderTypeProvider.Companion.EMPTY_VIEW
import ua.com.foxminded.newsfeed.ui.articles.adapter.ViewHolderTypeProvider.Companion.NYT_ARTICLE
import ua.com.foxminded.newsfeed.ui.articles.adapter.holders.*

class NewsRecyclerAdapter : ListAdapter<Article, NewsViewHolder>(NewsDiffCallback),
    ViewHolderTypeProvider {

    private val clickFlow = MutableSharedFlow<ClickEvent>(extraBufferCapacity = 1)

    fun getClickFlow(): Flow<ClickEvent> = clickFlow

    override fun getItemViewType(position: Int): Int = getItem(position).getViewHolderType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return when (viewType) {
            EMPTY_VIEW -> EmptyViewHolder(
                ItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            NYT_ARTICLE -> NytNewsHolder(
                ItemNytNewsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickFlow
            )
            CNN_ARTICLE -> CnnNewsHolder(
                ItemCnnNewsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickFlow
            )
            else -> WiredNewsHolder(
                ItemWiredNewsBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), clickFlow
            )
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
