package com.playgrounds.ynetfeed

import android.content.Context
import android.text.format.DateUtils
import com.playgrounds.ynetfeed.models.PostItemInfo
import com.prof.rssparser.Parser
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FeedParser(context: Context) {
    private var parser: Parser = Parser.Builder()
        .context(context)
        .cacheExpirationMillis(DateUtils.DAY_IN_MILLIS) // one day
        .build()

    suspend fun fetch(url: String): List<PostItemInfo> {
        return try {
            val channel = parser.getChannel(url)
            channel.articles.map {
                PostItemInfo(url, it.title ?: "", it.description?.substringAfterLast(">") ?: "", parseTimeStamp(it.pubDate), it.link ?: "")
            }
        } catch (exception: Exception) {
            listOf()
        }
    }

    private fun parseTimeStamp(pubDate: String?): Long {
        val now: Long = System.currentTimeMillis()
        if (pubDate == null) {
            return now
        }

        return try {
            val formatter: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ROOT)
            val date: Date = formatter.parse(pubDate) ?: Date()
            date.time
        } catch (exception: Exception) {
            now
        }
    }
}
