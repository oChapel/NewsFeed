package ua.com.foxminded.newsfeed.ui.articles.article

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
            loadUrl(args.article.link)
        }
    }
}
