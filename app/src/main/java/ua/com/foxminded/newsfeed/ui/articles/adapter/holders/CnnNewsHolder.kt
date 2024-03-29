package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.databinding.ItemCnnNewsBinding
import ua.com.foxminded.newsfeed.models.dto.Article
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.util.Utils

class CnnNewsHolder(
    private val binding: ItemCnnNewsBinding,
    private val clickFlow: MutableSharedFlow<ClickEvent>
) : NewsViewHolder<Article>(binding.root), View.OnClickListener {

    private var article: Article? = null

    init {
        binding.cnnNewsRootView.setOnClickListener(this)
        binding.cnnNewsBookmark.setOnClickListener(this)
    }

    override fun bind(article: Article) {
        this.article = article
        if (article.hasImageLink()) {
            Picasso.get().load(article.enclosure.link).into(binding.cnnNewsImage)
        } else {
            binding.cnnNewsImage.visibility = View.GONE
        }
        binding.cnnNewsTitle.text = article.title
        binding.cnnNewsDescription.text = article.description
        binding.cnnNewsTimespan.text = Utils.getTimeSpanString(article.pubDate)

        if (article.isBookmarked) {
            binding.cnnNewsBookmark.setImageResource(R.drawable.ic_bookmark_saved)
        } else {
            binding.cnnNewsBookmark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    override fun onClick(view: View) {
        article?.let {
            when (view) {
                binding.cnnNewsRootView -> clickFlow.tryEmit(ClickEvent.OnItemClicked(it))
                binding.cnnNewsBookmark -> clickFlow.tryEmit(ClickEvent.OnBookmarkClicked(it))
                else -> {}
            }
        }
    }
}