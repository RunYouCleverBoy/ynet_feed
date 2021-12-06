package com.playgrounds.ynetfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val titleMutableStateFlow = MutableStateFlow("")
    val titleFlow = titleMutableStateFlow.asStateFlow()

    fun postString(text: String) {
        titleMutableStateFlow.tryEmit(text)
    }
}
