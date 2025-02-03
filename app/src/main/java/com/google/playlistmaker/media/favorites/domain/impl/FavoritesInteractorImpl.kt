package com.google.playlistmaker.media.favorites.domain.impl

import com.google.playlistmaker.media.media.domain.model.MediaTrack
import com.google.playlistmaker.media.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.media.favorites.domain.usecase.FavoritesInteractor
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addToFavorites(track: MediaTrack) {
        repository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: MediaTrack) {
        repository.deleteFromFavorites(track)
    }

    override fun getFavoritesTracks(): Flow<List<MediaTrack>> {
        return repository.getFavoritesTracks()
    }

    override suspend fun getFavoritesId(): List<Int> {
        return repository.getFavoritesId()
    }

}