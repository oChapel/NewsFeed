package ua.com.foxminded.newsfeed.ui.articles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.com.foxminded.newsfeed.data.dto.NewsItem
import ua.com.foxminded.newsfeed.data.dto.NewsItem.Companion.CNN_ARTICLE
import ua.com.foxminded.newsfeed.data.dto.NewsItem.Companion.EMPTY_VIEW
import ua.com.foxminded.newsfeed.data.dto.NewsItem.Companion.NYT_ARTICLE
import ua.com.foxminded.newsfeed.databinding.ItemCnnNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemEmptyBinding
import ua.com.foxminded.newsfeed.databinding.ItemNytNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemWiredNewsBinding
import ua.com.foxminded.newsfeed.ui.articles.adapter.holders.*

class NewsRecyclerAdapter : ListAdapter<NewsItem, NewsViewHolder<NewsItem>>(NewsDiffCallback) {

    private val clickFlow = MutableSharedFlow<ClickEvent>(extraBufferCapacity = 1)

    fun getClickFlow(): Flow<ClickEvent> = clickFlow

    override fun getItemViewType(position: Int): Int = getItem(position).getViewHolderType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder<NewsItem> {
        val vh = when (viewType) {
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
        return vh as NewsViewHolder<NewsItem>
    }

    override fun onBindViewHolder(holder: NewsViewHolder<NewsItem>, position: Int) {
        holder.bind(getItem(position))
    }
}
