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
import ua.com.foxminded.newsfeed.ui.articles.adapter.holders.*

class NewsRecyclerAdapter(
    private val isEmptyScreenEnabled: Boolean
) : ListAdapter<Article, NewsViewHolder>(NewsDiffCallback) {

    private val clickFlow = MutableSharedFlow<ClickEvent>(extraBufferCapacity = 1)

    fun getClickFlow(): Flow<ClickEvent> = clickFlow

    override fun getItemViewType(position: Int): Int {
        return if (currentList.isEmpty()) {
            EMPTY_VIEW
        } else {
            when {
                getItem(position).link.contains(NYT_DOMAIN, ignoreCase = true) -> NYT_ARTICLE
                getItem(position).link.contains(CNN_DOMAIN, ignoreCase = true) -> CNN_ARTICLE
                getItem(position).link.contains(WIRED_DOMAIN, ignoreCase = true) -> WIRED_ARTICLE
                else -> UNKNOWN_ARTICLE
            }
        }
    }

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

    override fun getItemCount(): Int {
        //TODO java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionEmptyViewHolder
        if (isEmptyScreenEnabled) {
            if (currentList.isEmpty()) return 1
        }
        return currentList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        if (currentList.isNotEmpty()) {
            holder.bind(getItem(position))
        }
    }

    override fun onViewAttachedToWindow(holder: NewsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.setUpListeners()
    }

    override fun onViewDetachedFromWindow(holder: NewsViewHolder) {
        holder.clearListeners()
        super.onViewDetachedFromWindow(holder)
    }

    companion object {
        private const val EMPTY_VIEW = 0
        private const val NYT_ARTICLE = 1
        private const val CNN_ARTICLE = 2
        private const val WIRED_ARTICLE = 3
        private const val UNKNOWN_ARTICLE = 4

        private const val NYT_DOMAIN = "nytimes.com"
        private const val CNN_DOMAIN = "cnn.com"
        private const val WIRED_DOMAIN = "wired.com"
    }
}
