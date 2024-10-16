package com.google.playlistmaker.app

import android.app.Application
import com.google.playlistmaker.player.di.trackModule
import com.google.playlistmaker.search.di.searchModule
import com.google.playlistmaker.settings.di.settingsModule
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(searchModule, settingsModule, trackModule))
        }

        val switchTheme: SwitchThemeUseCase by inject()
        val theme = switchTheme.getTheme()
        switchTheme.execute(theme)
    }
}