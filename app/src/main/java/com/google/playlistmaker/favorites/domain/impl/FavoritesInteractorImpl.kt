package com.google.playlistmaker.favorites.domain.impl

import com.google.playlistmaker.favorites.data.entity.TrackEntity
import com.google.playlistmaker.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.favorites.domain.usecase.FavoritesInteractor
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository): FavoritesInteractor {
    override suspend fun addToFavorites(track: TrackEntity) {
        repository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: TrackEntity) {
        repository.deleteFromFavorites(track)
    }

    override fun getFavoritesTracks(): Flow<List<TrackEntity>> {
        return repository.getFavoritesTracks()
    }

    override suspend fun getFavoritesId(): List<Int> {
        return repository.getFavoritesId()
    }

}