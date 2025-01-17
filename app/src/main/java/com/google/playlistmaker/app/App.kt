package com.google.playlistmaker.app

import android.app.Application
import com.google.playlistmaker.db.di.dbModule
import com.google.playlistmaker.media.di.mediaModule
import com.google.playlistmaker.player.di.playerModule
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
            modules(
                listOf(
                    searchModule,
                    settingsModule,
                    playerModule,
                    mediaModule,
                    dbModule
                )
            )
        }

        val switchTheme: SwitchThemeUseCase by inject()
        val theme = switchTheme.getTheme()
        switchTheme.execute(theme)
    }
}