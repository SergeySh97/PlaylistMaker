package com.google.playlistmaker.settings.domain.api

interface SwitchThemeRepository {

    fun updateTheme(isNightMode: Boolean)

    fun getTheme(): Boolean
}