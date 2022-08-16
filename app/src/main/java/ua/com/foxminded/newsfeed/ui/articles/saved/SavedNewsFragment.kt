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
import ua.com.foxminded.newsfeed.data.Article
import ua.com.foxminded.newsfeed.data.NewsItem
import ua.com.foxminded.newsfeed.databinding.FragmentSavedNewsBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.articles.adapter.ClickEvent
import ua.com.foxminded.newsfeed.ui.articles.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.articles.saved.state.SavedNewsScreenState

class SavedNewsFragment : HostedFragment<
        SavedNewsContract.View,
        SavedNewsScreenState,
        SavedNewsScreenEffect,
        SavedNewsContract.ViewModel,
        SavedNewsContract.Host>(), SavedNewsContract.View {

    private var binding: FragmentSavedNewsBinding? = null
    private val newsAdapter = NewsRecyclerAdapter()
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            newsAdapter.currentList[viewHolder.adapterPosition].let {
                if (it is Article) model?.onArticleStateChanged(it)
            }
        }
    }
    private val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            newsAdapter.getClickFlow().collect {
                if (it is ClickEvent.OnItemClicked) {
                   findNavController().navigate(
                       SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                           it.article
                       )
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

    override fun showNews(list: List<NewsItem>) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        itemTouchHelper.attachToRecyclerView(null)
        binding = null
    }
}
