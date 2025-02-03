package com.google.playlistmaker.media.playlistcontent.ui.model

import com.google.playlistmaker.media.media.domain.model.Playlist

interface PlaylistContentState {
    data object Launch: PlaylistContentState
    data class Content(val playlist: Playlist): PlaylistContentState
}