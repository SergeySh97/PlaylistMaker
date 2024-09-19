package com.google.playlistmaker.app

import com.google.playlistmaker.data.impl.HistoryTracksRepositoryImpl
import com.google.playlistmaker.data.impl.TracksRepositoryImpl
import com.google.playlistmaker.data.network.RetrofitNetworkClient
import com.google.playlistmaker.data.sharedprefs.SharedPrefsManager
import com.google.playlistmaker.domain.api.ClearHistoryRepository
import com.google.playlistmaker.domain.usecases.ClearHistoryUseCase
import com.google.playlistmaker.domain.api.GetHistoryRepository
import com.google.playlistmaker.domain.usecases.GetHistoryUseCase
import com.google.playlistmaker.domain.api.SaveHistoryRepository
import com.google.playlistmaker.domain.usecases.SaveHistoryUseCase
import com.google.playlistmaker.domain.usecases.TracksSearchUseCase
import com.google.playlistmaker.domain.api.SearchTracksRepository
import com.google.playlistmaker.domain.impl.ClearHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.GetHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.SaveHistoryUseCaseImpl
import com.google.playlistmaker.domain.impl.TracksSearchUseCaseImpl

object Creator {
    private fun getTracksRepository(): SearchTracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient)
    }
    fun providerTracksSearchUseCase(): TracksSearchUseCase {
        return TracksSearchUseCaseImpl(getTracksRepository())
    }
    private fun getHistoryRepository(): GetHistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager)
    }
    fun providerGetHistoryUseCase(): GetHistoryUseCase {
        return GetHistoryUseCaseImpl(getHistoryRepository())
    }
    private fun saveHistoryRepository(): SaveHistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager)
    }
    fun providerSaveHistoryUseCase(): SaveHistoryUseCase {
        return SaveHistoryUseCaseImpl(saveHistoryRepository())
    }
    private fun clearHistoryRepository(): ClearHistoryRepository {
        return HistoryTracksRepositoryImpl(SharedPrefsManager)
    }
    fun providerClearHistoryUseCase(): ClearHistoryUseCase {
        return ClearHistoryUseCaseImpl(clearHistoryRepository())
    }
}