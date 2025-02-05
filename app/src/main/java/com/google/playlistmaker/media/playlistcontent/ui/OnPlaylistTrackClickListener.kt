package com.google.playlistmaker.media.playlistcontent.ui

import com.google.playlistmaker.media.media.domain.model.MediaTrack

interface OnPlaylistTrackClickListener {
    fun onPlaylistTrackClick(track: MediaTrack)
    fun onPlaylistTrackLongClick(track: MediaTrack)
}