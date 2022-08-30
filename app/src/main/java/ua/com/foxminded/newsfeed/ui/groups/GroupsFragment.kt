package ua.com.foxminded.newsfeed.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.com.foxminded.newsfeed.databinding.FragmentGroupsBinding
import ua.com.foxminded.newsfeed.ui.articles.news.feed.SourceTypes

class GroupsFragment : Fragment(), View.OnClickListener {

    private var binding: FragmentGroupsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.groupNyt?.setOnClickListener(this)
        binding?.groupCnn?.setOnClickListener(this)
        binding?.groupWired?.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        findNavController().navigate(
            GroupsFragmentDirections.actionGroupsFragmentToGroupFragment(
                when (view) {
                    binding?.groupNyt -> SourceTypes.NYT_FEED
                    binding?.groupCnn -> SourceTypes.CNN_FEED
                    else -> SourceTypes.WIRED_FEED
                }
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
