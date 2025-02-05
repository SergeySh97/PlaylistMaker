package com.google.playlistmaker.media.media.di

import androidx.room.Room
import com.google.playlistmaker.media.creator.AppDatabase
import com.google.playlistmaker.media.creator.data.impl.CreatorRepositoryImpl
import com.google.playlistmaker.media.creator.domain.api.CreatorRepository
import com.google.playlistmaker.media.creator.domain.impl.CreatorInteractorImpl
import com.google.playlistmaker.media.creator.domain.usecase.CreatorInteractor
import com.google.playlistmaker.media.creator.ui.viewmodel.CreatorVM
import com.google.playlistmaker.media.editor.ui.viewmodel.EditorVM
import com.google.playlistmaker.media.favorites.data.impl.FavoritesRepositoryImpl
import com.google.playlistmaker.media.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.media.favorites.domain.impl.FavoritesInteractorImpl
import com.google.playlistmaker.media.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.media.favorites.ui.viewmodel.FavoriteVM
import com.google.playlistmaker.media.playlistcontent.ui.viewmodel.PlaylistContentVM
import com.google.playlistmaker.media.playlists.ui.viewmodel.PlaylistVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mediaModule = module {

    viewModelOf(::EditorVM)
    viewModelOf(::PlaylistVM)
    viewModelOf(::PlaylistContentVM)
    viewModelOf(::FavoriteVM)
    viewModelOf(::CreatorVM)

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "playlistMaker.db").build()
    }

    singleOf(::FavoritesRepositoryImpl) { bind<FavoritesRepository>() }

    factoryOf(::CreatorInteractorImpl) { bind<CreatorInteractor>() }
    factoryOf(::CreatorRepositoryImpl) { bind<CreatorRepository>() }
    factoryOf((::FavoritesInteractorImpl)) { bind<FavoritesInteractor>() }
}