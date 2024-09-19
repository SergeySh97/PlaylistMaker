package com.google.playlistmaker.data.converters

import com.google.playlistmaker.data.dto.TrackDto
import com.google.playlistmaker.domain.models.Track

object Converters {

    fun mapToDomain(trackDto: TrackDto): Track {
        return Track(
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis.toString(),
            artworkUrl100 = trackDto.artworkUrl100,
            trackId = trackDto.trackId,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
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