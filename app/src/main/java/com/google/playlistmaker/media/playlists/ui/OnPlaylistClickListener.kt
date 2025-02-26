package com.google.playlistmaker.media.playlists.ui

import com.google.playlistmaker.media.media.domain.model.Playlist

interface OnPlaylistClickListener {
    fun onPlaylistClick(playlist: Playlist)
}