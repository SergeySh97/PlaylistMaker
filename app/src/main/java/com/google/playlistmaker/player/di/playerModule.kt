package com.google.playlistmaker.player.di

import android.media.MediaPlayer
import android.os.Handler
import com.google.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.google.playlistmaker.player.domain.api.PlayerRepository
import com.google.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.player.ui.viewmodel.PlayerVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val playerModule = module {


    viewModelOf(::PlayerVM)

    factoryOf(::PlayerRepositoryImpl) { bind<PlayerRepository>() }

    factory { MediaPlayer() }

    @Suppress("DEPRECATION")
    single { Handler() }

    factoryOf(::PlayerInteractorImpl) { bind<PlayerInteractor>() }
}
