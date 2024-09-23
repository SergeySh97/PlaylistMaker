package com.google.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper

object ClickDebounce {
    private const val CLICK_DEBOUNCE_DELAY = 1000L
    private var isClickAllowed = true
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}