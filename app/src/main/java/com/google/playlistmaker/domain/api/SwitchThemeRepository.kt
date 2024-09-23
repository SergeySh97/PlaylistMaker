package com.google.playlistmaker.domain.api

interface SwitchThemeRepository {

    fun switchTheme(isNightMode: Boolean)

    fun isNightMode(): Boolean
}