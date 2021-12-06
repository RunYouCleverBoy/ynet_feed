package com.playgrounds.ynetfeed

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.playgrounds.ynetfeed.models.PostItemInfo
import kotlinx.coroutines.flow.collect

class FeedFragment : Fragment() {
    private val mainViewModel: ActivityViewModel by hiltNavGraphViewModels(R.id.navigation_xml)
    private lateinit var viewModel: FeedListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity()
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity.application))
            .get(FeedListViewModel::class.java)
        val busySign = view.findViewById<ProgressBar>(R.id.feed_list_progress)
        val recycler: RecyclerView = view.findViewById(R.id.feed_list_recycler)
        val adapter = FeedListAdapter()
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        adapter.onItemClicked = { item: PostItemInfo ->
            mainViewModel.postString(item.title)
            val action = WebViewFragmentDirections.actionFeedFragmentToWebViewFragment(item.uri)
            findNavController().navigate(action)
        }

        val uris = arguments?.getStringArray(FEED_URI_KEY)
            ?: throw IllegalArgumentException("Expected an argument with URI")

        uris.forEach { uri ->
            lifecycleScope.launchWhenResumed {
                viewModel.requestFeedFor(uri, 5 * DateUtils.SECOND_IN_MILLIS)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.resultsFlow.collect {
                adapter.submitList(it)
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.busyFlow.collect {
                busySign.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }

    }

    companion object {
        const val FEED_URI_KEY = "FeedUri"
    }
}

