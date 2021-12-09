package com.playgrounds.ynetfeed.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.launchRepeatOn(lifeCycleState: Lifecycle.State, lambda: suspend CoroutineScope.() -> Unit) =
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(lifeCycleState, lambda)
    }
