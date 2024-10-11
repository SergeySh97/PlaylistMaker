package com.google.playlistmaker.app

import android.app.Application
import com.google.playlistmaker.app.Creator.setContext
/*import com.google.playlistmaker.di.searchModule
import com.google.playlistmaker.di.settingsModule
import com.google.playlistmaker.di.trackModule
import com.google.playlistmaker.settings.domain.usecase.SwitchThemeUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level*/

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setContext(applicationContext)//rm

        /*startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(searchModule, settingsModule, trackModule))
        }*/

        //val switchTheme: SwitchThemeUseCase by inject()
        val switchTheme = Creator.providerSwitchThemeUseCase()//rm
        val theme = switchTheme.getTheme()
        switchTheme.execute(theme)
    }
}