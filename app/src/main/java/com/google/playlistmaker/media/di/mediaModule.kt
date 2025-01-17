package com.google.playlistmaker.media.di

import com.google.playlistmaker.media.ui.viewmodel.PlaylistVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val mediaModule = module {

    viewModelOf(::PlaylistVM)
}