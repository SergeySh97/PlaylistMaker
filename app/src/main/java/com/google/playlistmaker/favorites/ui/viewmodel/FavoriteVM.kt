package com.google.playlistmaker.favorites.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.media.mapper.Mapper.toMediaTrack
import com.google.playlistmaker.favorites.ui.model.FavoriteState
import kotlinx.coroutines.launch

class FavoriteVM(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private val favoriteState = MutableLiveData<FavoriteState>()

    init {
        viewModelScope.launch {
            favoritesInteractor.getFavoritesTracks().collect { trackList ->
                if (trackList.isEmpty()) {
                    favoriteState.value = FavoriteState.NoTracks
                } else {
                    favoriteState.value = FavoriteState.HaveTracks(trackList
                        .map { it.toMediaTrack() }
                        .sortedByDescending { it.timestamp })
                }
            }
        }
    }

    fun getFavoritesState(): LiveData<FavoriteState> {
        return favoriteState
    }
}