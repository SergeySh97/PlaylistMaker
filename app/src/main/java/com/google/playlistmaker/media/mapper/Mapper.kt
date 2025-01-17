package com.google.playlistmaker.media.mapper

import com.google.playlistmaker.favorites.data.entity.TrackEntity
import com.google.playlistmaker.media.ui.model.MediaTrack
import com.google.playlistmaker.search.domain.model.Track

object Mapper {

    fun TrackEntity.toMediaTrack() = MediaTrack(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis.toString(),
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName ?: "null",
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        timestamp = timestamp
    )

    fun MediaTrack.toTrack() = Track(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}