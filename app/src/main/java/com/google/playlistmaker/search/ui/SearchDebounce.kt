package com.google.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper

object SearchDebounce {

    private const val SEARCH_DEBOUNCE_DELAY = 2000L
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    fun searchDebounce(searchRunnable: Runnable) {
        mainThreadHandler.removeCallbacks(searchRunnable)
        mainThreadHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}