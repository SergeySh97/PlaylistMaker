package com.google.playlistmaker.media.ui.model


interface FavoriteState {
    data class HaveTracks(val trackList: List<MediaTrack>): FavoriteState
    data object NoTracks: FavoriteState
}
