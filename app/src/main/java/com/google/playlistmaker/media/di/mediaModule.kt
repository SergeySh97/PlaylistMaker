package com.google.playlistmaker.media.di

import com.google.playlistmaker.media.ui.viewmodel.FavoriteVM
import com.google.playlistmaker.media.ui.viewmodel.PlaylistVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel { (favoriteList: String) ->
        FavoriteVM(favoriteList)
    }
    viewModel { (playlist: String) ->
        PlaylistVM(playlist)
    }
}