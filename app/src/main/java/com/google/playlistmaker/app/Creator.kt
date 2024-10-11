package com.google.playlistmaker.app

import android.content.Context
import android.media.MediaPlayer
import com.google.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.google.playlistmaker.player.domain.api.PlayerRepository
import com.google.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.google.playlistmaker.player.domain.usecase.PlayerInteractor
import com.google.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.google.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.google.playlistmaker.search.data.network.ItunesApi
import com.google.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.playlistmaker.search.data.sharedprefs.SharedPrefsManager
import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.impl.ClearHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.GetHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.SaveHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.TracksSearchUseCaseImpl
import com.google.playlistmaker.search.domain.usecase.ClearHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.GetHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SaveHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.TracksSearchUseCase
import com.google.playlistmaker.settings.domain.api.SwitchThemeRepository
import com.google.playlistmaker.settings.domain.impl.SwitchThemeUseCaseImpl
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase
import com.google.playlistmaker.settings.ui.thememanager.SwitchThemeRepositoryImpl
import com.google.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.google.playlistmaker.sharing.domain.usecase.SharingInteractor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Creator {
    private lateinit var appContext: Context
    fun setContext(context: Context) {
        appContext = context.applicationContext
    }
    val retrofit = Retrofit.Builder()
        .baseUrl(ItunesApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val itunesApi: ItunesApi = retrofit.create(ItunesApi::class.java)
    val mediaPlayer = MediaPlayer()

    private fun getTracksRepository(): SearchTracksRepository {
        return SearchTracksRepositoryImpl(RetrofitNetworkClient(itunesApi, appContext))
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

    private fun externalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(appContext)
    }

    fun providerSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(externalNavigator(), appContext)
    }

    private fun playerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(mediaPlayer)
    }

    fun providerPlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(playerRepository())
    }
}//rm