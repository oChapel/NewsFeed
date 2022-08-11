package ua.com.foxminded.newsfeed.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ua.com.foxminded.newsfeed.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private var binding: FragmentArticleBinding? = null
    private val args: ArticleFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.webView?.apply {
            webViewClient = WebViewClient()
            try {
                loadUrl(args.article.link)
            } catch (error: Exception) {
                error.printStackTrace()
                showErrorScreen()
            }
        }
    }

    private fun showErrorScreen() {
        binding?.articleErrorScreen?.animate()?.alpha(1F)
            ?.withStartAction {
                binding?.webView?.visibility = View.INVISIBLE
                binding?.articleErrorScreen?.visibility = View.VISIBLE
            }?.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val KEY_STRING_ARTICLE = "article"
    }
}
