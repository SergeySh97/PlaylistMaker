package com.google.playlistmaker.favorites.di

import androidx.room.Room
import com.google.playlistmaker.favorites.AppDatabase
import com.google.playlistmaker.favorites.data.impl.FavoritesRepositoryImpl
import com.google.playlistmaker.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.favorites.domain.impl.FavoritesInteractorImpl
import com.google.playlistmaker.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.favorites.ui.viewmodel.FavoriteVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dbModule = module {

    viewModelOf(::FavoriteVM)

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
    singleOf(::FavoritesRepositoryImpl) { bind<FavoritesRepository>() }
    factoryOf((::FavoritesInteractorImpl)) { bind<FavoritesInteractor>() }

}