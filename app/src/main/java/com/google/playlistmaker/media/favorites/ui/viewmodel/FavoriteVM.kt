package com.google.playlistmaker.media.favorites.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.playlistmaker.media.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.media.favorites.ui.model.FavoriteState
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
                        .sortedByDescending { it.timestamp })
                }
            }
        }
    }

    fun getFavoritesState(): LiveData<FavoriteState> = favoriteState
}