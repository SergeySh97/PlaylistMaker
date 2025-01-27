package com.google.playlistmaker.media.ui

import com.google.playlistmaker.media.domain.model.MediaTrack

interface OnMediaTrackClickListener {
    fun onTrackClick(mediaTrack: MediaTrack)
}