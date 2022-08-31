package ua.com.foxminded.newsfeed.ui.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.com.foxminded.newsfeed.databinding.FragmentArticleBinding

class ArticleFragment : Fragment(), View.OnClickListener {

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
        binding?.articleErrorOkBtn?.setOnClickListener(this)
        binding?.articleErrorTryAgainBtn?.setOnClickListener(this)

        binding?.webView?.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                setProgressVisibility(false)
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError?
            ) {
                if (error?.description != ERR_BLOCKED_BY_RESPONSE) {
                    view?.loadUrl("about:blank")
                    setErrorScreenVisibility(true)
                }
            }
        }
        loadArticle()
    }

    override fun onClick(view: View) {
        when (view) {
            binding?.articleErrorOkBtn -> findNavController().popBackStack()
            binding?.articleErrorTryAgainBtn -> {
                setErrorScreenVisibility(false)
                loadArticle()
            }
        }
    }

    private fun loadArticle() {
        setProgressVisibility(true)
        binding?.webView?.loadUrl(args.article.link)
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        val targetAlpha = if (isVisible) 1F else 0F
        val progressBar = binding?.articleProgressBar
        if (progressBar?.alpha != targetAlpha) {
            progressBar?.animate()?.alpha(targetAlpha)
                ?.withStartAction(
                    if (isVisible) Runnable { progressBar.visibility = View.VISIBLE } else null
                )
                ?.setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                ?.withEndAction(
                    if (isVisible) null else Runnable { progressBar.visibility = View.INVISIBLE }
                )
        }
    }

    private fun setErrorScreenVisibility(isVisible: Boolean) {
        val targetAlpha = if (isVisible) 1F else 0F
        val webView = binding?.webView
        val errorScreen = binding?.articleErrorScreen
        if (errorScreen?.alpha != targetAlpha) {
            errorScreen?.animate()?.alpha(targetAlpha)
                ?.withStartAction(
                    if (isVisible) Runnable { errorScreen.visibility = View.VISIBLE } else null
                )
                ?.setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                ?.withEndAction(
                    if (isVisible) null else Runnable { errorScreen.visibility = View.INVISIBLE }
                )

            webView?.animate()?.alpha(1 - targetAlpha)
                ?.withStartAction(
                    if (isVisible) null else Runnable { webView.visibility = View.VISIBLE }
                )
                ?.setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                ?.withEndAction(
                    if (isVisible) Runnable { webView.visibility = View.INVISIBLE } else null
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val ERR_BLOCKED_BY_RESPONSE = "net::ERR_BLOCKED_BY_RESPONSE"
    }
}
