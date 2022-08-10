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

    override fun bind(article: Article) {
        this.article = article
        if (article.enclosure.link != EMPTY_STRING) {
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

    override fun setUpListeners() {
        binding.nytNewsRootView.setOnClickListener(this)
        binding.nytNewsBookmark.setOnClickListener(this)
    }

    override fun clearListeners() {
        binding.nytNewsRootView.setOnClickListener(null)
        binding.nytNewsBookmark.setOnClickListener(null)
    }

    override fun onClick(view: View) {
        when (view) {
            binding.nytNewsRootView -> clickFlow.tryEmit(ClickEvent.OnItemClicked(article!!))
            binding.nytNewsBookmark -> clickFlow.tryEmit(ClickEvent.OnBookmarkClicked(article!!))
        }
    }
}