package com.playgrounds.ynetfeed

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class LandingPageFragment : Fragment() {
    private val mainViewModel: ActivityViewModel by hiltNavGraphViewModels(R.id.navigation_xml)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_landing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name: TextView = view.findViewById(R.id.landing_name)
        val date: TextView = view.findViewById(R.id.landing_date)
        val title: TextView = view.findViewById(R.id.landing_title)
        val goButton: Button = view.findViewById(R.id.landing_confirm_button)

        lifecycleScope.launchWhenResumed {
            date.text = getString(R.string.date_field_format, DateFormat.getDateFormat(requireContext()).format(Date()))
        }

        lifecycleScope.launchWhenResumed {
            mainViewModel.titleFlow.collect {
                title.text = it
            }
        }

        name.text = getString(R.string.name_field_format, "Rocky Raccoon")
        goButton.setOnClickListener {
            findNavController().navigate(R.id.tabbedFragment)
        }
    }
}
