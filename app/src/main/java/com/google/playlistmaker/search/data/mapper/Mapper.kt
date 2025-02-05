package com.google.playlistmaker.search.data.mapper

import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.search.data.dto.TrackDto
import com.google.playlistmaker.search.domain.model.Track

object Mapper {

    fun TrackDto.toTrack() = Track(
        trackName = trackName ?: "Без названия",
        artistName = artistName ?: "Неизвестный автор",
        trackTimeMillis = trackTimeMillis.toString(),
        artworkUrl100 = artworkUrl100 ?: "",
        trackId = trackId ?: 0,
        collectionName = collectionName ?: "null",
        releaseDate = releaseDate ?: "Неизвестно",
        primaryGenreName = primaryGenreName ?: "Неизвестно",
        country = country ?: "Неизветсно",
        previewUrl = previewUrl ?: "null"
    )

    fun Track.toTrackDto() = TrackDto(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis.toLong(),
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )

    fun Track.toMediaTrack(timestamp: Long) = MediaTrack(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl,
        timestamp = timestamp,
        isFavorite = isFavorite
    )
}