package com.google.playlistmaker.media.media.ui

import com.google.playlistmaker.media.media.domain.model.MediaTrack

interface OnMediaTrackClickListener {
    fun onTrackClick(mediaTrack: MediaTrack)
}