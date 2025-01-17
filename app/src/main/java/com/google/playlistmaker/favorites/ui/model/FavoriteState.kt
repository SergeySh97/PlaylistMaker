package com.google.playlistmaker.favorites.ui.model

import com.google.playlistmaker.media.ui.model.MediaTrack


interface FavoriteState {
    data class HaveTracks(val trackList: List<MediaTrack>): FavoriteState
    data object NoTracks: FavoriteState
}
