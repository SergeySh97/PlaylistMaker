package com.google.playlistmaker.media.playlists.ui.model

import com.google.playlistmaker.media.media.domain.model.Playlist

interface PlaylistsState {
    data class Content(val playlistList: List<Playlist>) : PlaylistsState
    data object Empty : PlaylistsState
}