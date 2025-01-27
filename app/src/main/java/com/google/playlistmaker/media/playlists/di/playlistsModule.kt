package com.google.playlistmaker.media.playlists.di

import com.google.playlistmaker.media.playlists.ui.viewmodel.PlaylistVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val playlistsModule = module {

    viewModelOf(::PlaylistVM)
}