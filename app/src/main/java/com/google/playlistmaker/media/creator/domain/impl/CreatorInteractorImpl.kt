package com.google.playlistmaker.media.creator.domain.impl

import com.google.playlistmaker.media.creator.domain.api.CreatorRepository
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.domain.model.MediaTrack
import com.google.playlistmaker.media.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class CreatorInteractorImpl(private val repository: CreatorRepository) : CreatorInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        repository.updatePlaylist(playlist)
    }

    override suspend fun addTrackIntoPlaylist(track: MediaTrack, playlist: Playlist) {
        repository.addTrackIntoPlaylist(track, playlist)
    }
}