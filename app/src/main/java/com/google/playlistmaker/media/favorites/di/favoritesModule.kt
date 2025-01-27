package com.google.playlistmaker.media.favorites.di

import com.google.playlistmaker.media.favorites.data.impl.FavoritesRepositoryImpl
import com.google.playlistmaker.media.favorites.domain.api.FavoritesRepository
import com.google.playlistmaker.media.favorites.domain.impl.FavoritesInteractorImpl
import com.google.playlistmaker.media.favorites.domain.usecase.FavoritesInteractor
import com.google.playlistmaker.media.favorites.ui.viewmodel.FavoriteVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val favoritesModule = module {

    viewModelOf(::FavoriteVM)

    singleOf(::FavoritesRepositoryImpl) { bind<FavoritesRepository>() }
    factoryOf((::FavoritesInteractorImpl)) { bind<FavoritesInteractor>() }

}