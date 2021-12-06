package com.playgrounds.ynetfeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabbedFragment : Fragment() {
    private lateinit var viewModel: TabbedViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tabbed_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = requireActivity()
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(activity.application))
            .get(TabbedViewModel::class.java)
        val tabs: TabLayout = view.findViewById(R.id.tabbed_tabs)
        val pager: ViewPager2 = view.findViewById(R.id.tabbed_pager)
        val pagerAdapter = PagerAdapter(this, viewModel.uris)
        pager.adapter = pagerAdapter
        val tabTexts = arrayOf(R.string.cars, R.string.social_and_culture)
        TabLayoutMediator(tabs, pager) { tab, position -> tab.setText(tabTexts[position]) }.attach()
    }
}

class PagerAdapter(fragment: Fragment, private val uris: Array<Array<String>>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = uris.size

    override fun createFragment(position: Int): Fragment {
        val feedFragment = FeedFragment()
        val bundle = Bundle()
        bundle.putStringArray(FeedFragment.FEED_URI_KEY, uris[position])
        return feedFragment.also { it.arguments = bundle }
    }

}
