package com.google.playlistmaker.media.playlists.domain.model

data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val filePath: String,
    val trackList: String,
    val tracksCount: Int
)
