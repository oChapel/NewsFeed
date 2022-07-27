package ua.com.foxminded.newsfeed.ui.saved_news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.data.model.Item
import ua.com.foxminded.newsfeed.databinding.FragmentSavedNewsBinding
import ua.com.foxminded.newsfeed.mvi.fragments.HostedFragment
import ua.com.foxminded.newsfeed.ui.NewsViewModelFactory
import ua.com.foxminded.newsfeed.ui.adapter.NewsRecyclerAdapter
import ua.com.foxminded.newsfeed.ui.saved_news.state.SavedNewsScreenEffect
import ua.com.foxminded.newsfeed.ui.saved_news.state.SavedNewsScreenState
import ua.com.foxminded.newsfeed.util.Constants

class SavedNewsFragment : HostedFragment<
        SavedNewsContract.View,
        SavedNewsScreenState,
        SavedNewsScreenEffect,
        SavedNewsContract.ViewModel,
        SavedNewsContract.Host>(), SavedNewsContract.View {

    private var binding: FragmentSavedNewsBinding? = null
    private val newsAdapter = NewsRecyclerAdapter()

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

        setListeners()
        attachItemTouchHelper()
    }

    override fun showNews(list: List<Item>) {
        setUpVisibility(true)
        newsAdapter.setNews(list)
        binding?.savedNewsRecyclerView?.layoutManager?.scrollToPosition(0)
        for (article in list) {
            newsAdapter.notifyItemChanged(newsAdapter.newsList.indexOf(article), true)
        }
    }

    override fun showEmptyScreen() {
        setUpVisibility(false)
    }

    override fun showToast(stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }

    override fun showUndoSnackBar(article: Item) {
        binding?.root?.let {
            Snackbar.make(it, R.string.article_deleted, Snackbar.LENGTH_LONG).apply {
                setAction(R.string.undo) {
                    model?.onArticleStateChanged(article)
                }
                show()
            }
        }
    }

    private fun setUpVisibility(areNewsPresent: Boolean) {
        binding?.let {
            if (areNewsPresent) {
                it.savedNewsRecyclerView.visibility = View.VISIBLE
                it.savedNewsEmptyScreen.visibility = View.INVISIBLE
            } else {
                it.savedNewsRecyclerView.visibility = View.INVISIBLE
                it.savedNewsEmptyScreen.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        newsAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                Bundle().apply {
                    putSerializable(Constants.KEY_STRING_ARTICLE, it)
                }
            )
        }

        newsAdapter.setOnBookmarkClickListener {
            model?.onArticleStateChanged(it)
        }
    }

    private fun attachItemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article = newsAdapter.newsList[viewHolder.adapterPosition]
                model?.onArticleStateChanged(article)
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding?.savedNewsRecyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
