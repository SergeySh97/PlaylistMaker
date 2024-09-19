package com.google.playlistmaker.ui.utils

import android.view.View

object Extensions {
    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }
}