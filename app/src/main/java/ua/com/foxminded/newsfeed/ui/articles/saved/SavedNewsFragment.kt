package ua.com.foxminded.newsfeed.ui.articles.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.dto.Article
import ua.com.foxminded.newsfeed.databinding.FragmentSavedNewsBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.ui.articles.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.articles.article.ArticleFragment
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState

class SavedNewsFragment : HostedFragment<
        SavedNewsContract.View,
        SavedNewsScreenState,
        SavedNewsScreenEffect,
        SavedNewsContract.ViewModel,
        SavedNewsContract.Host>(), SavedNewsContract.View {

    private var binding: FragmentSavedNewsBinding? = null
    private val newsAdapter = NewsRecyclerAdapter(true)
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val article = newsAdapter.currentList[viewHolder.adapterPosition]
            model?.onArticleStateChanged(article)
        }
    }
    private val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            newsAdapter.getClickFlow().collect {
                if (it is ClickEvent.OnItemClicked) {
                    findNavController().navigate(
                        R.id.action_savedNewsFragment_to_articleFragment,
                        Bundle().apply {
                            putSerializable(ArticleFragment.KEY_STRING_ARTICLE, it.article)
                        }
                    )
                } else if (it is ClickEvent.OnBookmarkClicked) {
                    model?.onArticleStateChanged(it.article)
                }
            }
        }
    }

    override fun createModel(): SavedNewsContract.ViewModel {
        return ViewModelProvider(this, NewsViewModelFactory())[SavedNewsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.savedNewsRecyclerView?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        itemTouchHelper.attachToRecyclerView(binding?.savedNewsRecyclerView)
    }

    override fun showNews(list: List<Article>) {
        newsAdapter.submitList(list)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    override fun showUndoSnackBar(article: Article) {
        binding?.root?.let {
            Snackbar.make(it, R.string.article_deleted, Snackbar.LENGTH_LONG).apply {
                setAction(R.string.undo) {
                    model?.onArticleStateChanged(article)
                }
                show()
            }
        }
    }

    /*private fun setUpVisibility(areNewsPresent: Boolean) {
        val recyclerView = binding?.savedNewsRecyclerView
        val emptyScreen = binding?.savedNewsEmptyScreen
        val targetAlpha = if (areNewsPresent) 1F else 0F
        val animationDuration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        if (targetAlpha != recyclerView?.alpha) {
            recyclerView?.animate()?.alpha(targetAlpha)
                ?.withStartAction(if (areNewsPresent) Runnable { recyclerView.visibility = View.VISIBLE} else null)
                ?.setDuration(animationDuration)
                ?.withEndAction(if (areNewsPresent) null else Runnable { recyclerView.visibility = View.INVISIBLE })
                ?.start()

            emptyScreen?.animate()?.alpha(1 - targetAlpha)
                ?.withStartAction(if (areNewsPresent) null else Runnable { emptyScreen.visibility = View.VISIBLE })
                ?.setDuration(animationDuration)
                ?.withEndAction(if (areNewsPresent) Runnable { emptyScreen.visibility = View.INVISIBLE } else null)
        }
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        itemTouchHelper.attachToRecyclerView(null)
        binding = null
    }
}
