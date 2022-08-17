package ua.com.foxminded.newsfeed.ui.articles.adapter.holders

import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.MutableSharedFlow
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.databinding.ItemNytNewsBinding
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.util.Utils

class NytNewsHolder(
    private val binding: ItemNytNewsBinding,
    private val clickFlow: MutableSharedFlow<ClickEvent>
) : NewsViewHolder(binding.root), View.OnClickListener {

    private var article: Article? = null

    init {
        binding.nytNewsRootView.setOnClickListener(this)
        binding.nytNewsBookmark.setOnClickListener(this)
    }

    override fun bind(article: Article) {
        this.article = article
        if (article.hasImageLink()) {
            Picasso.get().load(article.enclosure.link).into(binding.nytNewsImage)
        } else {
            binding.nytNewsImage.visibility = View.GONE
        }
        binding.nytNewsTitle.text = article.title
        binding.nytNewsDescription.text = article.description
        binding.nytNewsTimespan.text = Utils.getTimeSpanString(article.pubDate)

        if (article.isSaved) {
            binding.nytNewsBookmark.setImageResource(R.drawable.ic_bookmark_saved)
        } else {
            binding.nytNewsBookmark.setImageResource(R.drawable.ic_bookmark)
        }
    }

    override fun onClick(view: View) {
        article?.let {
            when (view) {
                binding.nytNewsRootView -> clickFlow.tryEmit(ClickEvent.OnItemClicked(it))
                binding.nytNewsBookmark -> clickFlow.tryEmit(ClickEvent.OnBookmarkClicked(it))
                else -> {}
            }
        }
    }
}
