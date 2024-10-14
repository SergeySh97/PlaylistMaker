package com.google.playlistmaker.settings.di

import androidx.lifecycle.ViewModel
import com.google.playlistmaker.settings.domain.api.SwitchThemeRepository
import com.google.playlistmaker.settings.domain.impl.SwitchThemeUseCaseImpl
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase
import com.google.playlistmaker.settings.ui.thememanager.SwitchThemeRepositoryImpl
import com.google.playlistmaker.settings.ui.viewmodel.SettingsVM
import com.google.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.google.playlistmaker.sharing.domain.ExternalNavigator
import com.google.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.google.playlistmaker.sharing.domain.usecase.SharingInteractor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module



val settingsModule = module {


    viewModelOf(::SettingsVM) { bind<ViewModel>() }
    singleOf(::SwitchThemeRepositoryImpl) { bind<SwitchThemeRepository>() }
    singleOf(::ExternalNavigatorImpl) { bind<ExternalNavigator>() }
    factoryOf(::SwitchThemeUseCaseImpl) { bind<SwitchThemeUseCase>() }
    factoryOf(::SharingInteractorImpl) { bind<SharingInteractor>() }
}

