package com.google.playlistmaker.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast

object Extensions {
    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun Context.showLongToast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
    fun Context.showShortToast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun TextView.setTrackText(count: Int) {
        text = when {
            count % 10 == 1 && count % 100 != 11 -> "$count $TRACK_1"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "$count $TRACK_2_4"
            else -> "$count $TRACK_ELSE"
        }
    }

    private const val TRACK_1 = "трек"
    private const val TRACK_2_4 = "трека"
    private const val TRACK_ELSE = "треков"

    fun TextView.setTimeText(count: Int) {
        text = when {
            count % 10 == 1 && count % 100 != 11 -> "$count $MINUTE_1"
            count % 10 in 2..4 && count % 100 !in 12..14 -> "$count $MINUTE_2_4"
            else -> "$count $MINUTE_ELSE"
        }
    }

    private const val MINUTE_1 = "минута"
    private const val MINUTE_2_4 = "минуты"
    private const val MINUTE_ELSE = "минут"
}