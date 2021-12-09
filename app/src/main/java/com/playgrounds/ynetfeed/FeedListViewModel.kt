package com.playgrounds.ynetfeed

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.playgrounds.ynetfeed.models.PostItemInfo
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FeedListViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = FeedRepository(app)

    private val ongoingUris = mutableSetOf<String>()
    private val mutableUiEventFlow = MutableSharedFlow<UiEvent>()

    val uiEventsFlow: SharedFlow<UiEvent> = mutableUiEventFlow

    fun processUri(uri: String) {
        viewModelScope.launch {
            requestFeedFor(uri, 5 * DateUtils.SECOND_IN_MILLIS)
        }
    }

    sealed interface UiEvent {
        data class NewData(val data: List<PostItemInfo>) : UiEvent
        data class BusyUpdate(val busy: Boolean) : UiEvent
    }

    private suspend fun requestFeedFor(uri: String, minPeriod: Long) {
        coroutineScope {
            while (isActive) {
                ongoingUris.add(uri)
                mutableUiEventFlow.emit(UiEvent.BusyUpdate(true))
                val data = UiEvent.NewData(repository.request(uri))
                mutableUiEventFlow.emit(data)
                ongoingUris.remove(uri)
                mutableUiEventFlow.emit(UiEvent.BusyUpdate(ongoingUris.isNotEmpty()))
                delay(minPeriod)
            }
        }
    }
}
