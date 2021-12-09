package com.playgrounds.ynetfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.playgrounds.ynetfeed.models.PostItemInfo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive

class FeedListViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = FeedRepository(app)
    private val resultsFlowMutable = MutableStateFlow<List<PostItemInfo>>(listOf())

    private val ongoingUris = mutableSetOf<String>()
    private val busyFlowMutable = MutableStateFlow(false)

    val resultsFlow = resultsFlowMutable.asStateFlow()
    val busyFlow = busyFlowMutable.asStateFlow()

    suspend fun requestFeedFor(uri: String, minPeriod: Long) {
        coroutineScope {
            while (isActive) {
                ongoingUris.add(uri)
                busyFlowMutable.emit(true)
                resultsFlowMutable.emit(repository.request(uri))
                ongoingUris.remove(uri)
                busyFlowMutable.tryEmit(ongoingUris.isNotEmpty())
                delay(minPeriod)
            }
        }
    }

}
