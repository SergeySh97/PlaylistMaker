package com.google.playlistmaker.media.creator.domain.api

import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface CreatorRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun updatePlaylist(playlist: Playlist)

    suspend fun addTrackIntoPlaylist(track: MediaTrack, playlist: Playlist)

    suspend fun deleteTrackFromPlaylist(track: MediaTrack, playlist: Playlist): Flow<Playlist>

}
