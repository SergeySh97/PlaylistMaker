package com.google.playlistmaker.media.mapper

import com.google.playlistmaker.media.creator.data.entity.PlaylistEntity
import com.google.playlistmaker.media.creator.data.entity.TrackInPlaylistEntity
import com.google.playlistmaker.media.domain.model.MediaTrack
import com.google.playlistmaker.media.favorites.data.entity.TrackEntity
import com.google.playlistmaker.media.playlists.domain.model.Playlist
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

    fun MediaTrack.toTrackEntity() = TrackEntity(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis.toLong(),
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
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

    fun MediaTrack.toTrackInPlaylistEntity() = TrackInPlaylistEntity(
        trackId = trackId,
        artworkUrl100 = artworkUrl100,
        trackName = trackName,
        artistName = artistName,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        trackTimeMillis = trackTimeMillis.toLong(),
        previewUrl = previewUrl,
        timestamp = timestamp
    )

    fun Playlist.toPlaylistEntity() =
        PlaylistEntity(
            playlistId = playlistId,
            name = name,
            description = description,
            filePath = filePath,
            trackList = trackList,
            tracksCount = tracksCount
        )

    fun PlaylistEntity.toPlaylist() =
        Playlist(
            playlistId = playlistId,
            name = name,
            description = description,
            filePath = filePath,
            trackList = trackList,
            tracksCount = tracksCount
        )
}