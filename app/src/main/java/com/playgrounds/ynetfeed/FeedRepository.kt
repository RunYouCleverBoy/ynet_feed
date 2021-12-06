package com.playgrounds.ynetfeed

import android.content.Context
import com.playgrounds.ynetfeed.models.PostItemInfo

class FeedRepository(appContext: Context) {
    private var parser = FeedParser(appContext)
    private val data = mutableMapOf<String, List<PostItemInfo>>()

    suspend fun request(uri: String): List<PostItemInfo> {
        val request = parser.fetch(uri)
        data[uri] = request
        return data.values.flatten()
    }
}
