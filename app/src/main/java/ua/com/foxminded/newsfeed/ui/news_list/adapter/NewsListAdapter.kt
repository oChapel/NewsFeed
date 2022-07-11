package ua.com.foxminded.newsfeed.ui.news_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.databinding.ItemNewsBinding

class NewsListAdapter : RecyclerView.Adapter<NewsListAdapter.NewsHolder>() {

    private val newsList = ArrayList<Item>()

    fun setNews(recentNews: List<Item>) {
        val oldNews = ArrayList<Item>(newsList)
        this.newsList.clear()
        this.newsList.addAll(recentNews)
        DiffUtil.calculateDiff(DiffUtilCallback(oldNews, newsList))
            .dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        return NewsHolder(
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(article: Item) {
            article.enclosure.link?.let {
                if (it != "") {
                    Picasso.get().load(it).into(binding.newsImage)
                }
            }
            binding.newsTitle.text = article.title
            binding.newsDescription.text = article.description
            binding.newsPubDateTv.text = article.pubDate
            binding.newsSource.text = "The New York Times"
        }
    }
}