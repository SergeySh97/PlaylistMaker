package com.google.playlistmaker.domain.usecases

interface SwitchThemeUseCase {

    fun switchTheme(isNightMode: Boolean)

    fun isNightMode(): Boolean
}