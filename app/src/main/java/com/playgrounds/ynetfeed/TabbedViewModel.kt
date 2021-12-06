package com.playgrounds.ynetfeed

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TabbedViewModel(app: Application) : AndroidViewModel(app) {

    val uris: Array<Array<String>> = arrayOf(
        arrayOf("https://www.ynet.co.il/Integration/StoryRss550.xml"),
        arrayOf("https://www.ynet.co.il/Integration/StoryRss3.xml", "http://www.ynet.co.il/Integration/StoryRss538.xml")
    )
}
