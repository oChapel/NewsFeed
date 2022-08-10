package ua.com.foxminded.newsfeed.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ua.com.foxminded.newsfeed.App
import ua.com.foxminded.newsfeed.R
import ua.com.foxminded.newsfeed.databinding.ActivityNewsBinding
import ua.com.foxminded.newsfeed.di.DaggerAppComponent
import ua.com.foxminded.newsfeed.ui.articles.news.NewsListContract
import ua.com.foxminded.newsfeed.ui.error.ErrorFragment

class NewsActivity : AppCompatActivity(), NewsListContract.Host {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.component = DaggerAppComponent.create()

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val navView: BottomNavigationView = binding.bottomNavView
        val navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newsListFragment, R.id.groupsFragment, R.id.savedNewsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showErrorDialog(error: Throwable) {
        error.printStackTrace()
        ErrorFragment.newInstance(
            R.string.error_dialog_title, R.string.error_dialog_message, android.R.string.ok
        ).apply {
            setError(error)
            show(supportFragmentManager, ErrorFragment.TAG)
        }
    }
}
