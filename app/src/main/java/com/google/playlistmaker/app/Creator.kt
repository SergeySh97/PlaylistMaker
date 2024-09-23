package com.google.playlistmaker.app

import android.content.Context
import com.google.playlistmaker.data.impl.HistoryTracksRepositoryImpl
import com.google.playlistmaker.data.impl.TracksRepositoryImpl
import com.google.playlistmaker.data.network.RetrofitNetworkClient
import com.google.playlistmaker.data.sharedprefs.SharedPrefsManager
import com.google.playlistmaker.domain.api.HistoryRepository
import com.google.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.google.playlistmaker.domain.usecases.GetHistoryUseCase
import com.google.playlistmaker.domain.usecases.SaveHistoryUseCase
import com.google.playlistmaker.domain.usecases.TracksSearchUseCase
import com.google.playlistmaker.domain.api.SearchTracksRepository
import com.google.playlistmaker.domain.api.SwitchThemeRepository
import com.google.playlistmaker.domain.impl.ClearHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.GetHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.SaveHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.SwitchThemeUseCaseImpl
import com.google.playlistmaker.domain.impl.TracksSearchUseCaseImpl
import com.google.playlistmaker.domain.usecases.SwitchThemeUseCase
import com.google.playlistmaker.presentation.thememanager.SwitchThemeRepositoryImpl

object Creator {
    private lateinit var appContext: Context
    fun setContext(context: Context) {
        appContext = context.applicationContext
    }
    private fun getTracksRepository(): SearchTracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient)
    }
    fun providerTracksSearchUseCase(): TracksSearchUseCase {
        return TracksSearchUseCaseImpl(getTracksRepository())
    }
    private fun getHistoryRepository(): HistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager(appContext))
    }
    fun providerGetHistoryUseCase(): GetHistoryUseCase {
        return GetHistoryUseCaseImpl(getHistoryRepository())
    }
    private fun saveHistoryRepository(): HistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager(appContext))
    }
    fun providerSaveHistoryUseCase(): SaveHistoryUseCase {
        return SaveHistoryUseCaseImpl(saveHistoryRepository())
    }
    private fun clearHistoryRepository(): HistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager(appContext))
    }
    fun providerClearHistoryUseCase(): ClearHistoryUseCase {
        return ClearHistoryUseCaseImpl(clearHistoryRepository())
    }
    private fun switchThemeRepository(): SwitchThemeRepository {
        return SwitchThemeRepositoryImpl(appContext)
    }
    fun providerSwitchThemeUseCase(): SwitchThemeUseCase {
        return SwitchThemeUseCaseImpl(switchThemeRepository())
    }
}