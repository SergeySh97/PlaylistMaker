package com.google.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteVM(
    favoriteList: String
): ViewModel() {

    private val favoriteListLiveData = MutableLiveData(favoriteList)

    fun observeFavoriteList(): LiveData<String> {
        return favoriteListLiveData
    }
}