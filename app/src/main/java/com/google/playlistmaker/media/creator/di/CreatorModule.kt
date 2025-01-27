package com.google.playlistmaker.media.creator.di

import androidx.room.Room
import com.google.playlistmaker.media.creator.AppDatabase
import com.google.playlistmaker.media.creator.data.impl.CreatorRepositoryImpl
import com.google.playlistmaker.media.creator.domain.api.CreatorRepository
import com.google.playlistmaker.media.creator.domain.impl.CreatorInteractorImpl
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.creator.ui.viewmodel.CreatorVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val creatorModule = module {

    viewModelOf(::CreatorVM)

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlistMaker.db").build()
    }

    factoryOf(::CreatorInteractorImpl) { bind<CreatorInteractor>() }

    factoryOf(::CreatorRepositoryImpl) { bind<CreatorRepository>() }
}