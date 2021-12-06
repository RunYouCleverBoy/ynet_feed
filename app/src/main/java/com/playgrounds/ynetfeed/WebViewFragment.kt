package com.playgrounds.ynetfeed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class WebViewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wv = view.findViewById<WebView>(R.id.wv)
        val arguments = arguments
        val navController = findNavController()
        if (arguments == null) {
            navController.popBackStack()
            return
        }

        val uri: String = WebViewFragmentArgs.fromBundle(arguments).openWithUrl
        with(wv.settings) {
            javaScriptEnabled = true
            databaseEnabled = true
            domStorageEnabled = true
        }
        wv.webViewClient = WebViewClient()

        wv.loadUrl(uri)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            isEnabled = if (wv.canGoBack()) {
                wv.goBack()
                true
            } else {
                navController.popBackStack()
                false
            }
        }
    }

}
