package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.databinding.ItemWiredNewsBinding
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.util.Utils

class WiredNewsHolder(
    private val binding: ItemWiredNewsBinding,
    private val clickFlow: MutableSharedFlow<ClickEvent>
) : NewsViewHolder<Article>(binding.root), View.OnClickListener {

    private var article: Article? = null

    init {
        binding.wiredNewsRootView.setOnClickListener(this)
        binding.wiredNewsBookmark.setOnClickListener(this)
    }

    override fun bind(article: Article) {
        this.article = article
        article.enclosure.thumbnail?.let {
            Picasso.get().load(it).into(binding.wiredNewsImage)
        } ?: run { binding.wiredNewsImage.visibility = View.GONE }
        binding.wiredNewsTitle.text = article.title
        binding.wiredNewsDescription.text = article.description
        binding.wiredNewsTimespan.text = Utils.getTimeSpanString(article.pubDate)

        if (article.isBookmarked) {
            binding.wiredNewsBookmark.setImageResource(R.drawable.ic_bookmark_saved)
        } else {
            binding.wiredNewsBookmark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    override fun onClick(view: View) {
        article?.let {
            when (view) {
                binding.wiredNewsRootView -> clickFlow.tryEmit(ClickEvent.OnItemClicked(it))
                binding.wiredNewsBookmark -> clickFlow.tryEmit(ClickEvent.OnBookmarkClicked(it))
                else -> {}
            }
        }
    }
}