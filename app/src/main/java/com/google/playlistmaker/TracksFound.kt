package com.google.playlistmaker

data class TracksFound(
    val resultCount: Int,
    val results: ArrayList<Track>
)

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String
)
