package com.google.playlistmaker.ui.track

import com.google.playlistmaker.domain.models.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}