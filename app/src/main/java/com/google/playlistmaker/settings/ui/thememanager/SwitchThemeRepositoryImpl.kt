package com.google.playlistmaker.settings.ui.thememanager

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.playlistmaker.settings.domain.api.SwitchThemeRepository

class SwitchThemeRepositoryImpl(context: Context) : SwitchThemeRepository {
    private val prefs = context.getSharedPreferences(PLAYLIST_MAKER, Context.MODE_PRIVATE)

    override fun updateTheme(isNightMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isNightMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        prefs.edit {
            putBoolean(APP_THEME, isNightMode).apply()
        }
    }

    override fun getTheme(): Boolean {
        return prefs.getBoolean(APP_THEME, true)
    }

    private companion object {
        const val APP_THEME = "app_theme"
        const val PLAYLIST_MAKER = "playlist_maker_prefs"
    }
}