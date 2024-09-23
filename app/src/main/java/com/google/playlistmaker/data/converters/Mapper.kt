package com.google.playlistmaker.data.converters

import com.google.playlistmaker.data.dto.TrackDto
import com.google.playlistmaker.domain.models.Track

object Mapper {

    fun mapToDomain(trackDto: TrackDto): Track {
        return Track(
            trackName = trackDto.trackName ?: "Без названия",
            artistName = trackDto.artistName ?: "Неизвестный автор",
            trackTimeMillis = trackDto.trackTimeMillis.toString(),
            artworkUrl100 = trackDto.artworkUrl100 ?: "",
            trackId = trackDto.trackId ?: 0,
            collectionName = trackDto.collectionName ?: "null",
            releaseDate = trackDto.releaseDate ?: "Неизвестно",
            primaryGenreName = trackDto.primaryGenreName ?: "Неизвестно",
            country = trackDto.country ?: "Неизветсно",
            previewUrl = trackDto.previewUrl ?: "null"
        )
    }

    fun mapToData(track: Track): TrackDto {
        return TrackDto(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis.toLong(),
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

}