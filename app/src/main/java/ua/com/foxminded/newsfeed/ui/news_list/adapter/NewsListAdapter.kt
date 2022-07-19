package ua.com.foxminded.newsfeed.ui.news_list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.databinding.ItemCnnNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemNytNewsBinding
import ua.com.foxminded.newsfeed.databinding.ItemWiredNewsBinding
import ua.com.foxminded.newsfeed.util.Util

class NewsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val newsList = ArrayList<Item>()
    private var onItemCLickListener: ((Item) -> Unit)? = null

    fun setNews(recentNews: List<Item>) {
        val oldNews = ArrayList<Item>(newsList)
        this.newsList.apply {
            clear()
            addAll(recentNews)
            sortByDescending { it.pubDate }
        }
        DiffUtil.calculateDiff(DiffUtilCallback(oldNews, newsList))
            .dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            newsList[position].link.contains(NYT_DOMAIN, ignoreCase = true) -> NYT_ARTICLE
            newsList[position].link.contains(CNN_DOMAIN, ignoreCase = true) -> CNN_ARTICLE
            newsList[position].link.contains(WIRED_DOMAIN, ignoreCase = true) -> WIRED_ARTICLE
            else -> UNKNOWN_ARTICLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NYT_ARTICLE -> NytNewsHolder(
                ItemNytNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            CNN_ARTICLE -> CnnNewsHolder(
                ItemCnnNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> WiredNewsHolder(
                ItemWiredNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            NYT_ARTICLE -> (holder as NytNewsHolder).bind(newsList[position])
            CNN_ARTICLE -> (holder as CnnNewsHolder).bind(newsList[position])
            WIRED_ARTICLE -> (holder as WiredNewsHolder).bind(newsList[position])
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun setOnItemClickListener(listener: (Item) -> Unit) {
        onItemCLickListener = listener
    }

    inner class NytNewsHolder(private val binding: ItemNytNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Item) {
            article.enclosure.link?.let {
                Picasso.get().load(it).into(binding.nytNewsImage)
            } ?: run { binding.nytNewsImage.visibility = View.GONE }
            binding.nytNewsTitle.text = article.title
            binding.nytNewsDescription.text = article.description
            binding.nytNewsTimespan.text = Util.getTimeSpanString(article.pubDate)
            binding.nytNewsRootView.setOnClickListener {
                onItemCLickListener?.let { it(article) }
            }
        }
    }

    inner class CnnNewsHolder(private val binding: ItemCnnNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Item) {
            article.enclosure.link?.let {
                Picasso.get().load(it).into(binding.cnnNewsImage)
            } ?: run { binding.cnnNewsImage.visibility = View.GONE }
            binding.cnnNewsTitle.text = article.title
            binding.cnnNewsDescription.text = article.description
            binding.cnnNewsTimespan.text = Util.getTimeSpanString(article.pubDate)
            binding.cnnNewsRootView.setOnClickListener {
                onItemCLickListener?.let { it(article) }
            }
        }
    }

    inner class WiredNewsHolder(private val binding: ItemWiredNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Item) {
            article.enclosure.thumbnail?.let {
                Picasso.get().load(it).into(binding.wiredNewsImage)
            } ?: run { binding.wiredNewsImage.visibility = View.GONE }
            binding.wiredNewsTitle.text = article.title
            binding.wiredNewsDescription.text = article.description
            binding.wiredNewsTimespan.text = Util.getTimeSpanString(article.pubDate)
            binding.wiredNewsRootView.setOnClickListener {
                onItemCLickListener?.let { it(article) }
            }
        }
    }

    companion object {
        private const val UNKNOWN_ARTICLE = 0
        private const val NYT_ARTICLE = 1
        private const val CNN_ARTICLE = 2
        private const val WIRED_ARTICLE = 3

        private const val NYT_DOMAIN = "nytimes.com"
        private const val CNN_DOMAIN = "cnn.com"
        private const val WIRED_DOMAIN = "wired.com"
    }
}