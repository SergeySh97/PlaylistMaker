package com.google.playlistmaker.search.di

import androidx.lifecycle.ViewModel
import com.google.playlistmaker.search.data.impl.HistoryTracksRepositoryImpl
import com.google.playlistmaker.search.data.impl.SearchTracksRepositoryImpl
import com.google.playlistmaker.search.data.network.ItunesApi
import com.google.playlistmaker.search.data.network.NetworkClient
import com.google.playlistmaker.search.data.network.RetrofitNetworkClient
import com.google.playlistmaker.search.data.sharedprefs.HistoryTracksManager
import com.google.playlistmaker.search.data.sharedprefs.SharedPrefsManager
import com.google.playlistmaker.search.domain.api.HistoryRepository
import com.google.playlistmaker.search.domain.api.SearchTracksRepository
import com.google.playlistmaker.search.domain.impl.ClearHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.GetHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.SaveHistoryUseCaseImpl
import com.google.playlistmaker.search.domain.impl.SearchTracksUseCaseImpl
import com.google.playlistmaker.search.domain.usecase.ClearHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.GetHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SaveHistoryUseCase
import com.google.playlistmaker.search.domain.usecase.SearchTracksUseCase
import com.google.playlistmaker.search.ui.viewmodel.SearchVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val searchModule = module {


    viewModelOf(::SearchVM) { bind<ViewModel>() }
    singleOf(::SearchTracksRepositoryImpl) { bind<SearchTracksRepository>() }
    singleOf(::SharedPrefsManager) { bind<HistoryTracksManager>() }
    singleOf(::HistoryTracksRepositoryImpl) { bind<HistoryRepository>() }
    factoryOf(::SearchTracksUseCaseImpl) { bind<SearchTracksUseCase>() }
    factoryOf(::GetHistoryUseCaseImpl) { bind<GetHistoryUseCase>() }
    factoryOf(::SaveHistoryUseCaseImpl) { bind<SaveHistoryUseCase>() }
    factoryOf(::ClearHistoryUseCaseImpl) { bind<ClearHistoryUseCase>() }
    factoryOf(::RetrofitNetworkClient) { bind<NetworkClient>() }
    single<ItunesApi> {
        get<Retrofit>().create(ItunesApi::class.java)
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ItunesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
