package ua.com.foxminded.newsfeed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ua.com.foxminded.newsfeed.databinding.ActivityNewsBinding
import ua.com.foxminded.newsfeed.di.DaggerAppComponent
import ua.com.foxminded.newsfeed.ui.news_list.NewsListContract

class NewsActivity : AppCompatActivity(), NewsListContract.Host {

    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.component = DaggerAppComponent.create()

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}