package com.google.playlistmaker.player.ui

import com.google.playlistmaker.search.domain.model.Track

interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}