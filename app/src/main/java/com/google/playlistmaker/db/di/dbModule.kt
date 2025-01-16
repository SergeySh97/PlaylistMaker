package com.google.playlistmaker.db.di

import androidx.room.Room
import com.google.playlistmaker.db.AppDatabase
import com.google.playlistmaker.db.data.impl.FavoritesRepositoryImpl
import com.google.playlistmaker.db.domain.api.FavoritesRepository
import com.google.playlistmaker.db.domain.impl.FavoritesInteractorImpl
import com.google.playlistmaker.db.domain.usecase.FavoritesInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
    singleOf(::FavoritesRepositoryImpl) { bind<FavoritesRepository>() }
    factoryOf((::FavoritesInteractorImpl)) { bind<FavoritesInteractor>() }

}